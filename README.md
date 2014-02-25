wicket-jpaプロジェクトの動作のさせ方

# 事前準備

JDK7以上を予めインストールしておいて下さい.  

このファイルが見られているということは、既にMavenがインストールされていることでしょう！

7以上が必須です.  

# eclipse対応にするには
次のコマンドで、eclipseプロジェクトが作成されます.  

```
mvn eclipse:eclipse
```

プロジェクトが作成されたらeclipseにインポートして下さい.  
また、プロジェクトが使用するJDKを7相当に変更して下さい.  

# Webアプリ起動方法

次のコマンドで、Webアプリが起動します.  

```
mvn compile exec:java
```

テストをスキップしたい場合は次のコマンド.  

```
mvn -DskipTests=true compile exec:java
```

```com.jabaraster.petshop.WebStarter```クラスを実行して下さい.  

コンソールに次の表示が出たら起動しています.  

```
********************************************************************
*** WARNING: Wicket is running in DEVELOPMENT mode.              ***
***                               ^^^^^^^^^^^                    ***
*** Do NOT deploy to your live server(s) without changing this.  ***
*** See Application#getConfigurationType() for more information. ***
********************************************************************
```


次のURLにアクセスすると、Webアプリが使えます.  

<http://localhost:8081>

自動でテーブルが作成される設定になっています.  
この挙動を変えたい場合、例えばテーブルを削除して再作成した場合、Javaの起動引数に次のような設定を追加して下さい.  

```
-Dhibernate.hbm2ddl.auto=create-drop
```

