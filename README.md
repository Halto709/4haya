# 目次
- [ゲーム概要][label1] 
- [セットアップマニュアル][label2]
- [ユーザーマニュアル][label3]
- [Author][label4]
  
[label1]:#ゲーム概要
# ゲーム概要
「4人で早押し」は、4人で早押しクイズを楽しむことができるポイント競争型ゲームです。  

[label2]:#セットアップマニュアル
# セットアップマニュアル
**サーバのアクセス**  
1)bashを起動後、下記のsshコマンドで実行します。
```
$ ssh isdev24@[IPアドレス]
```
アクセスが完了するとメッセージが表示されます。  
2)アクセスが出来たら以下のコマンドでsshアクセスログを確認します。  
```
$ sudo cat /var/log/auth.log
```
コマンドを入力するとパスワードの入力を求められる。  
**タイムゾーンの変更**  
1)タイムゾーンを設定するためにtimedatecltコマンドのset-timezoneオプションを使用する．使用後timedatecleを利用し確認する．以下にコマンドを示す．  
```
isdev24@ubuntuXXX:~$ sudo timedatectl set-timezone Asia/Tokyo
```
```
isdev24@ubuntuXXX:~$ timedatectl
```
**Javaのインストール**  
1)次のコマンドを実行しJava Corretoをインストールする準備をする。  
```
$ wget -O - https://apt.corretto.aws/corretto.key | sudo gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg && echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | sudo tee /etc/apt/sources.list.d/corretto.list

```
2)実行後、Press[ENTER] to countine or Ctrl-c to cancel.が表示さる。それ以降に進むにはENTERキーを押してください。  
3)次に以下のaptコマンドを利用してインストールをする  
```
$ sudo apt-get update; sudo apt-get install -y java-21-amazon-corretto-jdk
```
4)javaのインストールを確認するために以下のコマンドを実行する。  
```
$ java -version
```
-**Webアプリケーションの公開**  
-1)以下のコマンドでホームディレクトリに移動する。  
```
$cd
```
2)移動後ホームディレクトリに移動できているかの確認を以下のコマンドで行う。  
```
$pwd
```
3)ブラウザでGithubにアクセスし対象のリポジトリのURLをコピーする。('git@github.com:Halto709/yonhaya.git')。そこ後以下のコマンドでクローンする。  
```
$ git clone git@github.com:Halto709/yonhaya.git
```
4)リポジトリが正しくクローン出来たか確認するために以下のコマンドを実行する。  
```
isdev24@ubuntuXXX:~$ ls
```
コマンドを実行すると**yonhaya**というリポジトリがあるか確認する。  
5)以下のコマンドを利用しリポジトリを移動する。  
```
$cd yonhaya
```
6)最後にプロジェクトの実行のために以下のコマンドを実行する。  
```
$  bash ./gradlew bootrun
```
実行後、ブラウザで対象にURLにアクセスし確認する。  

[label3]:#ユーザーマニュアル
# ユーザーマニュアル
**ログイン方法について**

Webページにアクセスすると「クイズに参加」というボタンが表示される。  
そのボタンをクリックすると、ログイン画面に遷移する。  
遷移するとユーザー名とパスワードを要求される。  
以下にユーザー名とパスワードとそのユーザーに付与されるロールの一覧を示す。  

**[ユーザー名・パスワード・ロール一覧]**

ユーザー名:user1  
パスワード:isdev  
ロール:USER  

ユーザー名:user2  
パスワード:isdev  
ロール:USER  

ユーザー名:user3  
パスワード:isdev  
ロール:USER  

ユーザー名:user4  
パスワード:isdev  
ロール:USER  

ユーザー名:owner  
パスワード:isdev24  
ロール:OWNER  

・USERロールでは４人集まるとクイズを遊ぶことが出来る。  
・OWNERロールではクイズを途中で終わった場合などのエラーが発生した場合にそのゲームをリセットすることが出来る。


**クイズ(yonhaya)の遊び方**  
1.「クイズに参加」をクリックしてください。  
2. 上記のユーザ名とパスワードでログインしてください。  
3.「入室」をクリックしてください。  
4.参加者４人集まるまで待機してください。  
5.４人揃うとクイズが開始します。  
6.クイズ問題が出題されるので、正解だと思う選択肢を４択から選びクリックしてください。  
7.４人全員が回答すると次の問題に行きます。  
8.問題数は全部５門あります！  
9.回答が正解であれば、回答の速さに応じてポイントが加算されます!  

**試合結果履歴の表示**  
クイズ終了後にルームを退出から「これまでの試合結果」をクリックすることで試合結果履歴を閲覧することが出来ます。  
※注：初めてログインした場合にも「これまでの試合結果」が表示されます。  

[label4]:#Author
# Author
Z2635  中村　友大  
Z2722  長原　冬馬  
Z2386  鬼塚　大輔  
Z2444  灰藤　直紀  

