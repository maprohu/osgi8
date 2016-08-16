package osgi8.deployer

import java.io.File
import java.nio.file.{Path, Paths}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.stream.scaladsl.{FileIO, Sink, Source, StreamConverters}
import org.apache.maven.shared.invoker.{DefaultInvocationRequest, DefaultInvoker}
import sbt.io.IO
import sbt.io.Path._

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}
import scala.xml.{Node, NodeSeq, XML}

/**
  * Created by martonpapp on 25/06/16.
  */
object OsgiDeployer {

  def perform(bundle: Bundle, target: Uri, obr: Uri)(implicit
    actorSystem: ActorSystem,
    materializer: Materializer,
    executionContext: ExecutionContext
  ) = {
    runMaven(
      pom(
        Poms.singleDep(bundle),
        Poms.listDeps("target/deps.txt")
      ),
      "dependency:list"
    ) { dir =>
//      val deps = dir / "target" / "deps"
//      deps.mkdirs()

      val repoFile = dir / "target" / "repo"
      val repoUrl = repoFile.toURI.toString
      val depsString = IO
          .readLines(
            dir / "target" / "deps.txt"
          )

      val repoxmlfile = (repoFile / "repository.xml")

      val repoxmluri = target.withPath(target.path / "repository.xml")
      for {
        repoxml <- Http().singleRequest(
          HttpRequest(
            uri = repoxmluri
          )
        )
        _ <- if (repoxml.status.isSuccess()) {
          repoxml.entity.dataBytes.runWith(
            FileIO.toPath(repoxmlfile.toPath)
          )
        } else {
          Future()
        }
        done <- {

          val (ups, execs) = depsString
            .map({ line =>
              val f = line.trim.split(':')
              if (f.length == 6) {
                // windows has : in path
                f.take(4) ++ Array(s"${f(4)}:${f(5)}")
              } else {
                f
              }
            })
            .collect({
              case Array(group, artifact, _, version, path) =>
                val bnd = Bundle(group, artifact, version)

                val uploadname =
                  s"${group}-${artifact}-${version}.jar"

                val obruri = obr.withPath(
                  obr.path / uploadname
                )

                val uploadRequest =
                  HttpRequest(
                    method = HttpMethods.PUT,
                    uri = target.withPath(target.path / uploadname),
                    entity = HttpEntity.fromPath(ContentTypes.`application/octet-stream`, new File(path).toPath)
                  )

                val pomx =
                  <execution>
                    <id>
                      {path}
                    </id>
                    <phase>package</phase>
                    <goals>
                      <goal>deploy-file</goal>
                    </goals>
                    <configuration>
                      <url>
                        {repoUrl}
                      </url>
                      <file>
                        {path}
                      </file>{bnd.toXml}<packaging>bundle</packaging>
                      <bundleUrl>{obruri.toString()}</bundleUrl>
                    </configuration>
                  </execution>

                (uploadRequest, pomx)
            })
            .unzip

          runMaven(
            pom(
              <build>
                <plugins>
                  <plugin>
                    <groupId>org.apache.felix</groupId>
                    <artifactId>maven-bundle-plugin</artifactId>
                    <version>3.0.1</version>
                    <executions>
                      {execs}
                    </executions>
                  </plugin>
                </plugins>
              </build>
            ),
            "package"
          ) { _ =>

            val repoupxml = IO.read(repoxmlfile)

            Source.single(
              HttpRequest(
                method = HttpMethods.PUT,
                uri = repoxmluri,
                entity = HttpEntity(repoupxml)
              )
            )
              .concat(
                Source(ups)
              )
              .mapAsync(1)({ up =>
                Http().singleRequest(
                  up
                )
              })
              .runWith(Sink.foreach( out =>
                println(out)
              ))


          }

        }
      } yield {
        done
      }


    }

  }

  def runMaven[T](pomFileString: Node, goal : String)( andThen : File => T ) = {
    IO.withTemporaryDirectory { dir =>
      val pomFile = dir / "pom.xml"

      XML.save(pomFile.getAbsolutePath, pomFileString)

      val request = new DefaultInvocationRequest
      request.setPomFile(pomFile)
      request.setGoals( Seq( goal ) )
      val invoker = new DefaultInvoker

      val result = invoker.execute(request)

      if (result.getExitCode == 0) {
        andThen(dir)
      } else {
        throw new RuntimeException()
      }
    }
  }

  def pom(content: Node*) = {
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>

      <groupId>emsa</groupId>
      <artifactId>osgi-deployer-temp</artifactId>
      <version>1.0.0-SNAPSHOT</version>
      {content}
    </project>

  }


  object Poms {


    def dep(bundle: Bundle) = {
      <dependency>
        {bundle.toXml}
      </dependency>
    }

    def singleDep(bundle: Bundle) = {
      <dependencies>
        {dep(bundle)}
      </dependencies>
    }

    def listDeps(outputFile: String) = {
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <version>2.10</version>
            <configuration>
              <includeScope>runtime</includeScope>
              <outputFile>{outputFile}</outputFile>
              <outputScope>false</outputScope>
              <outputAbsoluteArtifactFilename>true</outputAbsoluteArtifactFilename>
            </configuration>
          </plugin>
        </plugins>
      </build>
    }

    def copyDep(bundle: Bundle, outputFile: String) = {
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <version>2.10</version>
            <executions>
              <execution>
                <id>copy</id>
                <phase>package</phase>
                <goals>
                  <goal>copy</goal>
                </goals>
                <configuration>
                  <artifactItems>
                    <artifactItem>
                      {bundle.toXml}
                      <outputDirectory>target</outputDirectory>
                      <destFileName>{outputFile}</destFileName>
                    </artifactItem>
                  </artifactItems>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    }

    def copyDeps(bundles: (Bundle, String)*) = {
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <version>2.10</version>
            <executions>
              <execution>
                <id>copy</id>
                <phase>package</phase>
                <goals>
                  <goal>copy</goal>
                </goals>
                <configuration>
                  <artifactItems>
                    {
                      bundles.map({ case (bnd, f) =>
                          copyDepArtifactItem(bnd, f)
                      })
                    }
                  </artifactItems>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    }

    def copyDepArtifactItem(bundle: Bundle, outputFile: String) =
      <artifactItem>
        {bundle.toXml}
        <outputDirectory>target</outputDirectory>
        <destFileName>{outputFile}</destFileName>
      </artifactItem>
  }


  def getBundle(bundle: Bundle)(action: File => Unit) = {
    getBundles((bundle, action))
//    runMaven(
//      pom(
//        Poms.copyDep(bundle, "dep.jar")
//      ),
//      "package"
//    ) { dir =>
//      action(dir / "target" / "dep.jar")
//    }

  }

  def getBundles(bundles: (Bundle, File => Unit)*) = {
    runMaven(
      pom(
        Poms.copyDeps(
          bundles.zipWithIndex.map({ case ((bnd, _), idx) =>
            (bnd, s"jar${idx}")
          }):_*
        )
      ),
      "package"
    ) { dir =>
      bundles.zipWithIndex.foreach({ case ((_, action), idx) =>
        action(dir / "target" / s"jar${idx}")
      })
    }

  }

  def getBundlesAll(bundles: Seq[Bundle])(fn: Seq[File] => Unit) = {
    val bundleFiles =
      bundles.zipWithIndex.map({ case (bnd, idx) =>
        (bnd, s"jar${idx}")
      })

    runMaven(
      pom(
        Poms.copyDeps(
          bundleFiles:_*
        )
      ),
      "package"
    ) { dir =>
      fn(
        bundleFiles.map({
          case (_ , file) =>
            dir / "target" / file
        })
      )
    }

    bundleFiles.map(_._2)
  }

}

case class Bundle(
  group: String,
  artifact: String,
  version: String,
  classifier : String = "bundle",
  `type` : String = "jar"
) {
  def toXml : NodeSeq =
    <groupId>{group}</groupId>
    <artifactId>{artifact}</artifactId>
    <version>{version}</version>
    <classifier>{classifier}</classifier>
    <type>{`type`}</type>

}