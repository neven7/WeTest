#!/bin/sh
#@hugang
#读配置文件
while read line;do
 eval "$line"
done < ./conf/runcase.conf
svn_account=$account
svn_password=$password
svn_project=$projectsvn
path=$basepath




#初始化
function init()
{
#1.生成临时uuid目录
uuid=`uuidgen`
local_path=$path/$uuid
echo $local_path
sudo mkdir $local_path
#2.拉代码到临时uuid目录
svn co --username  $svn_account  --password $svn_password --no-auth-cache  --non-interactive $projectsvn  $local_path	
#3.修改工程中ip和port
cat <<end >  $local_path/src/test/java/global.properties
url=$ip:$port
source=
retry=
end

#4.拷贝*.xml到工程中
projectpath=/data1/WeTest
cp -rf $projectpath/antbuild/*.xml $local_path
#5.进入uuid文件下
cd $local_path	
}


printf "******************************\n"
printf "先选择测试环境，再选择测试模块\n"
printf "******************************\n"

printf "1.选择环境\n"
#1.选择环境
select word in "预览环境" "线上环境" "自定义环境" "退出"
do
 case $word in
 "预览环境")
 ip=
 port=
  break;
  ;;
  "线上环境")
   ip=
   port=
   break;
   ;;
   "自定义环境")
    read -p "请输入IP: " ip
    read -p "请输入PORT: " port
   break;
   ;;
   "退出")
   printf "Bye\n"
   exit
   break;
   ;;
   *)
   printf "输入错误，请选1,2,3\n"
  ;;
 esac
done;
echo "word=$word"
#2.选择模块
printf "2.选择测试模块 \n"
select word in "模块1" "模块2" "退出"
do
 case $word in
 "模块1")
  init $ip $port
  ant -buildfile build_mail_status.xml &
  break;
  ;;
  "模块2")
  init $ip $port
  ant -buildfile build_mail_comment.xml &
   break;
   ;;
  
   "退出")
   printf "Bye\n"
   exit
   break;
   ;;
   *)
   printf "输入错误-请输入1-2\n"
  ;;
 esac
done;

exit 0
