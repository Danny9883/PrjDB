# PrjDB

## 드라이버 다운경로
https://mvnrepository.com/
  검색 : oracle
      Ojdbc11
  version 23.26.1.0.0
    Files : jar(7.3Mb)  다운로드
  
 sts Project : PrjDB 생성
   lib 폴더 만들고
   ojdbc11-23-26-1.0.0.jar 를 붙여넣는다


sts PrjDB project 선택, 마우스 우클릭
  Properties 선택
   -> Java Build Path 
   -> Libraries tab
   -> classpath 를 선택
     오른쪽 Add jar 버튼 클릭
       ojdbc11-23-26-1.0.0.jar 파일을 선택
  
  PrjDB 프로젝트에
    Referrenced Libraries 항목이 생긴다