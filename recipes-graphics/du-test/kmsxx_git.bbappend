FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
    https://github.com/pybind/pybind11/archive/v2.6.0.zip;name=pybind11 \
    https://wrapdb.mesonbuild.com/v1/projects/pybind11/2.6.0/1/get_zip;downloadfilename=pybind11-2.6.0-1-wrap.zip;name=wrap \
"

SRC_URI[pybind11.sha256sum] = "c2ed3fc84db08f40a36ce1d03331624ed6977497b35dfed36a1423396928559a"
SRC_URI[wrap.sha256sum] = "dd52c46ccfdbca06b6967e89c9981408c6a3f4ed3d50c32b809f392b4ac5b0d2"

do_configure:prepend() {
	cp -arp ${WORKDIR}/pybind11-2.6.0 ${WORKDIR}/git/subprojects/
}
