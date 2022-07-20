SUMMARY = "du test package group"
LICENCE = "MIT"

inherit packagegroup

PACKAGES = " \
    packagegroup-dutest \
"

RDEPENDS:${PN} += " \
    python3-crcmod \
    kmsxx \
    kmsxx-python \
    kms-tests \
"
