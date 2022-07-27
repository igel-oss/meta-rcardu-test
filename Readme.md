# Openembeded /Yocto BSP layer for R-Car DU test

([Click here for Japanese](Readme_jp.md))

## Overview
This layer provides recipes of kms-tests script for R-Car DU test for use with OpenEmbedded and/or Yocto.

## Dependency

This layer depends on meta-renesas layer:
- URL: https://github.com/renesas-rcar/meta-renesas
- branch: dunfell

## Build

Assume that the yocto environment has already been extracted to `/path/to/work/yocto`.

```bash
$ cd /path/to/work/yocto
$ ls
build  meta-openembedded  meta-renesas  poky
```

The `build` directory is present only if it has already been built.

### Add this layer and configure for test

Extract this recipe to `/path/to/work/yocto/`.

```bash
$ git clone https://github.com/igel-oss/meta-rcardu-test.git
```

Create `bblayer.conf` and `local.conf` according to [this build procedure](https://elinux.org/R-Car/Boards/Yocto-Gen3/v5.9.0#Manual_steps).

```bash
$ . poky/oe-init-build-env
$ cp ../meta-renesas/meta-rcar-gen3/docs/sample/conf/salvator-x/poky-gcc/mmp/*.conf ./conf/
$ mv conf/local-wayland.conf conf/local.conf
```

Add this layer to `bblayer.conf`.

```diff
 BBLAYERS ?= " \
   ${TOPDIR}/../poky/meta \
   ${TOPDIR}/../poky/meta-poky \
   ${TOPDIR}/../poky/meta-yocto-bsp \
   ${TOPDIR}/../meta-renesas/meta-rcar-gen3 \
   ${TOPDIR}/../meta-openembedded/meta-python \
   ${TOPDIR}/../meta-openembedded/meta-oe \
+  ${TOPDIR}/../meta-rcardu-test \
   "
```

Add the following to `local.conf` so that kms-tests is added to rootfs.

```
DISTRO_FEATURES_append = " dutest"
```

### Build rootfs image and SDK

Create rootfs and poky SDK.

```bash
$ bitbake core-image-weston
$ bitbake core-image-weston -c populate_sdk
```

## Build kms-tests standalone

Yocto build installs kms-tests to rootfs. So please skip this section if you don't need to build kms-tests standalone.

### Install poky SDK

Install the poky SDK on the host PC by executing `poky-glibc-x86_64-core-image-weston-aarch64-salvator-x-toolchain-<poky version>.sh` created in `build/tmp/deploy/sdk/`.
Default install path is `/opt/poky/<poky version>/`.

```bash
$ cd tmp/deploy/sdk/
$ ./poky-glibc-x86_64-core-image-weston-aarch64-salvator-x-toolchain-<poky version>.sh
```

### Build kms-tests

Assume kms-tests is located at `/path/to/work/kms-tests`.
Run make with `PYTHONPATH`, `CFLAGS`, and `LDFLAGS` set as follows:

```bash
$ cd /path/to/work/kms-tests
$ /opt/poky/<poky version>/environment-setup-aarch64-poky-linux
$ PYTHONPATH=`echo -e "import crcmod\nprint(crcmod.__path__[0])" | python3 | sed -r 's/(\/.*\/)crcmod/\1/'` CFLAGS="--sysroot=${SDKTARGETSYSROOT}" LDFLAGS="--sysroot=${SDKTARGETSYSROOT}" make
```

To install, set the installation path to `INSTALL_DIR` and run make install.

```
$ make install INSTALL_DIR=/path/to/target/directory
```

## Run Test on R-Car Board

After the board is started, execute script from the console.

```
root@salvator-x:~# kms-test-formats.py
```

kms-tests script install path is `/usr/bin/`
