mydir="$( cd "$(dirname "$0")" && pwd )"
#jscience_url='https://java.net/projects/jscience/downloads/download/jscience-4.3.1-bin.zip'
jscience_url='http://localhost:11000/jscience-4.3.1-bin.zip'
cache=_cache
jscience_zip="${cache}"/jscience-4.3.1-bin.zip
project_repo="${mydir}"/project_repo

[ -d "${cache}" ] || mkdir -pv "${cache}"

if [ ! -f "${jscience_zip}" ]
then
    false \
    || { which aria2c 1>/dev/null 2>&1 && aria2c ${jscience_url} -o ${jscience_zip} ; } \
    || { which wget   1>/dev/null 2>&1 && wget   ${jscience_url} -O ${jscience_zip} ; } \
    || { which curl   1>/dev/null 2>&1 && curl   ${jscience_url} -o ${jscience_zip} ; } \
    || echo 'aria2c, wget or curl not found in path. Download ' \
            "${jscience_url}"' manually into '"${jscience_zip}"'.'
fi

true \
&& rm -rf _tmp \
&& mkdir -pv _tmp \
&& cd _tmp \
&& unzip -q "${mydir}"/"${jscience_zip}" \
&& { [ -d "${mydir}"/lib ] || mkdir -pv "${mydir}"/lib ; } \
&& jscience_jar_path="$(find -name jscience.jar 2>/dev/null | head -1)" \
&& mv -v "${jscience_jar_path}" "${mydir}"/lib \
&& cd "${mydir}" \
&& { [ -d "${project_repo}" ] || mkdir -pv "${project_repo}" ; } \
&& mvn install:install-file \
       -Dfile='${basedir}/lib/jscience.jar' \
       -DgroupId=org.pseudosystems \
       -DartifactId=jscience \
       -Dversion=4.3.1 \
       -Dpackaging=jar \
       -DlocalRepositoryPath="${project_repo}" \
       -DcreateChecksum \
       -DupdateReleaseInfo \
&& rm -rf _tmp \
&& true

