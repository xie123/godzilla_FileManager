##文件夹管理器

### 文件的操作类型 和返回 数据 处理的不是很好。

### 注意
1.没有关注权限的申请


### - 使用
#### 读取
1. 请求扫描全部磁盘 (返回 各个磁盘的路径)(ps:请求后自动结束)
```
FileManager.with(this).transform(RequestFactory.RequestLocalDisk)
  			 .into(new Target.RequestLocalDiskTargetImpl() {
                    @Override
                    public void call(List<String> strings) {
                    	
                    }
                });             
```
2. 当前activity内动态注册 广播
```
      FileManager
                .with(this)
                .transform(RequestFactory.REQUEST_BROADCAST_RECEIVER_DISK)
                .into(new Target.RequestBroadcastReceiverDiskTargetImpl() {
                    @Override
                    public void call(Collection<String> diskAddPath) {
                     
                    }
                });
```

3. 获取指定磁盘下的文件信息 (ps:请求后自动结束)

	```
    FileManager.with(MainActivity.this).transform(RequestFactory.RequestScanDisk)
                .checkedDiskPath("指定路径或者集合")
                .into(new Target.RequestScanDiskTargetImpl() {
                    @Override
                    public void call(List<DiskInfo> diskInfos) {

                    }
                });

	```
4. 获取指定路径下的文件列表 (ps:请求后自动结束)
	```
  FileManager
                .with(this)
                .transform(RequestFactory.RequestFileListDisk)
                .checkedFilelist("").addErrorCallBack(throwable ->{})
                .into(new Target.RequestFileListDiskTargetImpl() {
                    @Override
                    public void call(List<FileInfo> fileInfos) {
                        
                    }
                });
 ```  
	文件类型
	```
	@StringDef({NONE,APK,IMG,VIDEO,MP3,TXT,WPS_WORD,WPS_EXCEL,WPS_PPT,WPS_PDF,FOLDER,RECOMMEND,ZIP})
	```
#### -操作
5. 删除文件/文件夹 (ps:请求后自动结束)
	```
   FileManager.with(this)
            .transform(RequestFactory.RequestDelectFile)
            .delectFilePath("")
             .into(new Target.RequestOperateFileImpl(){
                    @Override
                    public void call(/*删除成功返回删除目录 失败为空*/Boolean s) {
                      
                    }
                });
	```

6. 粘贴文件/文件夹
	```
   FileManager.with(getViewer().context())
                .transform(RequestFactory.RequestFileStick)
                .stickFilePath(filePath)
                .into(new Target.RequestOperateFileImpl(){
                    @Override
                    public void call(/*删除成功返回删除目录 失败为空*/Boolean s) {
                        getViewer().showToast(s?"粘贴成功!":"粘贴失败!");
                        getViewer().onRequestStick(s);
                    }
                });

	```

7. 新建文件夹
	```
 	FileManager.with(getViewer().context())
                .transform(RequestFactory.RequestNewFolder)
                .createFolder(mCurrentFolderPath,folderName)
                .into(new Target.RequestOperateFileImpl(){
                    @Override
                    public void call(/*删除成功返回删除目录 失败为空*/Boolean s) {
                        getViewer().showToast(s?"新建成功!":"新建失败!");
                        getViewer().onRequestCreateFolder(s,folderName);
                    }
                });
	```

8. 重命名
	```
 	FileManager.with(getViewer().context())
                .transform(RequestFactory.RequestRename)
                .rename(fileBeanVm.getModel().getPath(),name)
                .into(new Target.RequestOperateFileImpl(){
                    @Override
                    public void call(/*删除成功返回删除目录 失败为空*/Boolean s) {
                        getViewer().showToast(s?"重命名成功!":"重命名失败!");
                        getViewer().onRequestRename(s,name);
                    }
                });
	```

9. 复制
	```
 FileManager.init().getFileManagerConfiguration().getFileOperationHelper().copy(fileBeanVm.getModel().getPath());
	```

10. 剪切 
	```
 FileManager.init().getFileManagerConfiguration().getFileOperationHelper().cut(fileBeanVm.getModel().getPath());
	```

### -错误回调
 
 增加 `.addErrorCallBack(throwable ->{})`



### -初始化

1. `DiskInfoManager.class ` 文件管理器的信息类
	1. 存储了磁盘路径 
	2. 存储了相应磁盘路径的文件信息
	3. 初始化部分文件类型
		1. 如果需要重写类型 调用 ` public void addFileType(FileType fileType)` 进行覆盖
		
2. `FileManagerConfiguration.class` 文件管理的配置文件
	1. 是否显示移动磁盘
	2. 是否显示本地磁盘
	3. 文件读取/操作  超时 时间
	4. 是否读取隐藏文件
	5. 排序规则
	
3. `FileType.class ` 定义了文件类型
	1. 文件类型 type
	2. accept() 文件归类类型
	3. judge(java.lang.String filePath) 判断文件是否属于该类型
	4. additionalConfiguration 额外参数存储
		列如初始化 各种类型的 Drawble 
		```
     	FileManager.init().getDiskInfoManager().getFileTypeByType(IFileManagerFileType.FOLDER).addConfiguration(GlobalSettingConstants.FileClass.FILE_TYPE_DRAWABLE,R.drawable.img_folder);
		```

		```
        FileManager.init().getDiskInfoManager().getFileTypeByType(IFileManagerFileType.APK).addConfiguration(GlobalSettingConstants.FileClass.FILE_TYPE_FRAME,null);
       ```


### -说明
1. SDK 不知道写的怎么样。大概分三部分吧
	- 图1 描述的是 SDK中 控制`操作`跟随`Context`的生命周期走
	- 图2 描述的是 各种请求 各种操作。 Request 是具体的操作类。 Target 是相应类型的返回回调和数据处理。 不过目前还是很粗糙。
	- 图3 大概是些统计初始化 各个类型的划分

![图1](https://i.imgur.com/AFcYBD9.png)

![图2](https://i.imgur.com/RQl39EK.png)

![图3](https://i.imgur.com/JRWwUwy.png)
	



	


