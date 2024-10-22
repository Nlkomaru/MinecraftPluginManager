# コマンド

`mpm install <url>`
urlからプラグインをインストールします。
github, spigot, dev.bukkit, hanger, modrinthに対応しています。

`mpm uninstall <plugin>`
プラグインをアンインストールします。

`mpm list [--lock]`
インストールされているプラグインの一覧を表示します。
`--lock`をつけるとロックされているプラグインの一覧を表示します。

`mpm outdated <plugin>`
プラグインの更新を確認します。

`mpm outdatedAll`
インストールされているプラグインの更新を確認します。

`mpm update`
/mpm outdated によって新しいバージョンがあるとされたプラグインを更新します。

`mpm help`
ヘルプを表示します。

`mpm version`
バージョンを表示します。

`mpm reload`
コンフィグをリロードします。

`mpm info <plugin>`
プラグインの情報を表示します。

`mpm search <plugin>`
プラグインを検索します。

`mpm lock <plugin>`
プラグインをロックします。
ロックをしたプラグインは、`mpm outdatedAll`で更新されません。

`mpm unlock <plugin>`
プラグインのロックを解除します。

`mpm removeUnmanaged [--force]`
mpm管理下にないプラグインを削除します。
`--force`をつけると確認なしで削除します。


## Usage
まず初めに、`mpm install <url>`でプラグインをインストールします。<br>

インストールされたプラグインは`mpm list`で確認できます。<br>

個別のプラグインの更新を確認するには`mpm outdated <plugin>`を使用します。<br>

ですが、基本的には`mpm outdatedAll`で全てのプラグインの更新を確認することをおすすめします。<br>
また、`mpm outdated`で更新を確認した後は`mpm updateAll`で更新を行います。<br>

インストールされたプラグインをアンインストールするには`mpm uninstall <plugin>`を使用します。<br>

!!! warning
    mpm管理下にないプラグインは削除されるため、/plugins/minecraftPluginManager/local にプラグインを移動してください。
    この場合、`mpm updateAll`を実行時にプラグインがコピーされます。

