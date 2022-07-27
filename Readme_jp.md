# R-Car DU のテストをするための kms-tests を実施するための yocto レシピ

yocto 環境を `/path/to/work/yocto` に展開済みとします。

```bash
$ cd /path/to/work/yocto
$ ls
build  meta-openembedded  meta-renesas  poky
```
※`build` ディレクトリがあるのは、ビルド済みの場合のみ

## レイヤーの追加と設定

このレシピを `/path/to/work/yocto/` に展開します。

```bash
$ git clone https://github.com/igel-oss/meta-rcardu-test.git
```

[こちらの build 手順](https://elinux.org/R-Car/Boards/Yocto-Gen3/v5.9.0#Manual_steps) に従って、bblayer.conf と local.conf を作成します。

```bash
$ . poky/oe-init-build-env
$ cp ../meta-renesas/meta-rcar-gen3/docs/sample/conf/salvator-x/poky-gcc/mmp/*.conf ./conf/
$ mv conf/local-wayland.conf conf/local.conf
```

bblayer.conf と local.conf を変更します。
まず、bblayer.conf にこのレシピを追加します。
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

次に local.conf に以下を追加して、kms-tests のビルドに必要なパッケージが追加されるようにします。
```
DISTRO_FEATURES_append = " dutest"
```

## ビルド

rootfs と SDK をビルドをします。
```bash
$ bitbake core-image-weston
$ bitbake core-image-weston -c populate_sdk
```

## SDK の展開

`build/tmp/deploy/sdk/` に `poky-glibc-x86_64-core-image-weston-aarch64-salvator-x-toolchain-<poky version>.sh` が作成されているのでこれを実行して SDK をホストPCに展開します。デフォルトの展開先は `/opt/poky/<poky version>/` です。
```bash
$ cd tmp/deploy/sdk/
$ ./poky-glibc-x86_64-core-image-weston-aarch64-salvator-x-toolchain-<poky version>.sh
```

## kms-tests のビルド

kms-tests は yocto のビルドに含まれていますが、SDK を使ってビルドしたい場合は以下のようにします。kms-tests はダウンロード済みで /path/to/work/kms-tests にあるとします。

```bash
$ cd /path/to/work/kms-tests
$ /opt/poky/<poky version>/environment-setup-aarch64-poky-linux
$ PYTHONPATH=`echo -e "import crcmod\nprint(crcmod.__path__[0])" | python3 | sed -r 's/(\/.*\/)crcmod/\1/'` CFLAGS="--sysroot=${SDKTARGETSYSROOT}" LDFLAGS="--sysroot=${SDKTARGETSYSROOT}" make
$ make install INSTALL_DIR=/path/to/target/directory
```

`/path/to/target/directory` に展開されたファイルを rootfs にコピーすれば、kms-tests が実行できます。

## テストの実行

ボード起動後、コンソールから script を実行します。

```
root@salvator-x:~# kms-test-formats.py
```

※ script のインストール先は /usr/bin 以下です。