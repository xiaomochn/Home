# 家

智能家居的项目,用Android手机做中控端,otg链接了一个板子,下发指令上传数据,

现在已经完成基础搭建,

## 大概介绍下使用到的东西

Android原生开发,基础功能,otg通讯等原生功能
Weex 页面相关的大部分用Weex实现
微信小程序作为客户端暂时,没开发原生的

## 功能介绍

- [x] 终端(Android)和硬件设备(比如开关)通讯,能知道硬件的状态并且能控制
- [x] 终端将硬件和自身状态记录在本地,并同步在外网服务器一份
- [x] 终端添加用户(微信用户),添加过的用户可以通过微信(小程序)查看或者控制硬件
- [x] 终端和微信间有即时通讯能力,通过推送实现的
- [ ] 漂亮的UI

基础功能实现了,这一天流程是可以跑通的,但是稳定性,UI等等还差的远



## 目的

志在打造一套廉价的家具控制系统,终端使用的时候Android旧手机,用不了多少钱,硬件也是自己搞得每个开关下来不到20块钱,现在智能家居搞得火但不好用,定制化功能也弱又贵,这套系统随便折腾

## 有兴趣的一起搞起来
