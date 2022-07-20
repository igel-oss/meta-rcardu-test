SUMMARY = "Test suite for the Renesas R-Car DU display unit."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://GPL-COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

BRANCH = "master"
SRC_URI = "git://git.ideasonboard.com/renesas/kms-tests.git;branch=${BRANCH}"
SRCREV = "70afa1a51f4ffc4db404514af0b2990b23d56fee"

SRC_URI_append = " \
	file://0001-Support-Meson-build.patch \
	file://0002-Add-License-file.patch \
"

S = "${WORKDIR}/git"

DEPENDS = "kmsxx python3 python3-crcmod-native"

inherit python3native meson pkgconfig

RDEPENDS:${PN} = "python3-core python3-crcmod"
