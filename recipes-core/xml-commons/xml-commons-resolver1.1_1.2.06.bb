DESCRIPTION = "Library to resolve various public or system identifiers into accessible URLs (Java)"
AUTHOR = "Apache Software Foundation"
LICENSE = "Apache-2.0"
PR = "r1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d273d63619c9aeaf15cdaf76422c4f87"

SRC_URI = "http://archive.apache.org/dist/xml/commons/source/xml-commons-external-${PV}-src.tar.gz"

inherit java-library

S = "${WORKDIR}/xml-commons-external-${PV}"

DEPENDS = "fastjar-native jaxp1.3"

do_unpackpost[dirs] = "${B}"
do_unpackpost() {
  find src -exec \
    sed -i -e "s|@impl.name@|XmlResolver|" \
           -e "s|@impl.version@|1.2|" {} \;
}

addtask unpackpost after do_unpack before do_patch

JARFILENAME = "resolver.jar"
ALTJARFILENAMES = ""

do_compile() {
  mkdir -p build

  cp=${STAGING_DATADIR_JAVA}/jaxp1.3.jar

  javac -sourcepath src -d build -classpath $cp `find src -name "*.java" -and -not  -wholename "*tests*"`

  (cd src && find org -name "*.xml" -o -name "*.txt" -o -name "*.src" -exec cp {} ../build/{} \;)

  fastjar cfm ${JARFILENAME} src/manifest.resolver -C build  org
}

SRC_URI[sha256sum] = "2aafec42825d6e4193b667047b770f1aa9027dfcfc94742ca2d7ccefb6da714d"

BBCLASSEXTEND = "native"
