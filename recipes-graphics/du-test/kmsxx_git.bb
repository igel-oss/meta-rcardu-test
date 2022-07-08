SUMMARY = "C++ library for kernel mode setting"
HOMEPAGE = "https://github.com/tomba/kmsxx"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=815ca599c9df247a0c7f619bab123dad"

BRANCH = "master"
SRC_URI = "gitsm://github.com/tomba/kmsxx.git;protocol=https;branch=${BRANCH}"
SRCREV = "2236a8ccacdfed5ff5f6873ed6618eccf570193d"

SRC_URI_append = " \
    file://0001-Don-t-install-kmstest.patch \
"

DEPENDS = "libdrm python3 libevdev fmt"

PACKAGES =+ "${PN}-python"
FILES:${PN}-python += "${libdir}/python*/site-packages"

S = "${WORKDIR}/git"

inherit python3native meson pkgconfig
