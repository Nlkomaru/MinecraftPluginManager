# コマンド

- [x] `mpm install <url>`<br>
urlからプラグインをインストールします。<br>
github, jenkins, dev.bukkit, hanger, modrinthに対応しています。


- [ ] `mpm uninstall <plugin>`<br>
プラグインをアンインストールします。<br>

- [ ] `mpm list [--lock]`<br>
インストールされているプラグインの一覧を表示します。<br>
`--lock`をつけるとロックされているプラグインの一覧を表示します。

- [ ] `mpm outdated <plugin>`<br>
プラグインの更新を確認します。<br>

- [ ] `mpm outdatedAll`<br>
インストールされているプラグインの更新を確認します。<br>

- [ ] `mpm update`<br>
/mpm outdated によって新しいバージョンがあるとされたプラグインを更新します。<br>

- [ ] `mpm help`<br>
ヘルプを表示します。<br>

- [x] `mpm version`<br>
バージョンを表示します。<br>

- [ ] `mpm reload`<br>
コンフィグをリロードします。<br>

- [x] `mpm info <plugin>`<br>
プラグインの情報を表示します。<br>

- [ ] `mpm search <plugin>`<br>
プラグインを検索します。<br>

- [ ] `mpm lock <plugin>`<br>
プラグインをロックします。<br>
ロックをしたプラグインは、`mpm outdatedAll`で更新されません。<br>

- [ ] `mpm unlock <plugin>`<br>
プラグインのロックを解除します。<br>

- [ ] `mpm removeUnmanaged [--force]`<br>
mpm管理下にないプラグインを削除します。<br>
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

