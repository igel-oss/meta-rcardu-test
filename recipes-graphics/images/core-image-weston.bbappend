IMAGE_INSTALL_append += " \
    ${@bb.utils.contains('DISTRO_FEATURES', \
        'dutest', \
        'packagegroup-dutest', '', d)} \
"
