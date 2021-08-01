# 🌞Team. Weather Ranger - backend🌞

<div align=center>
  
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com/EFUB-SeeMe/SeeMe_Server&count_bg=%23E71B8E&title_bg=%23555555&icon=&icon_color=%23E7E7E7)](https://hits.seeyoufarm.com) 
</div>

<div align=center>

<img src = "https://user-images.githubusercontent.com/68282057/127765868-d29a1154-c7d2-4c95-ab77-e1a72bbef5b2.png" height="200px">
  </div>

# 🌈 백엔드 팀원 소개

| [강민지](https://github.com/nitronium102)                    | [박주은](https://github.com/hoit1302)                        | [서수경](https://github.com/sukyeongs)                       |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **![img](https://lh4.googleusercontent.com/fO4iZwIoHdE2Nz9BPVrkDoHo1f7YcpNnnOrUxKnJx8Z3rBPfMhB9AtCSD3qku0U00GirxfRJfUg58T8VIiEl4CXrYnsJL_-Ryoc6MY-q63szPSU8tEme7rMKQ2EpGY-h095BA-Nc)** | ![image](https://user-images.githubusercontent.com/68107000/127767511-acbf8118-dbed-44e8-98ff-2251d3bdb53a.png) | **![img](https://lh3.googleusercontent.com/vFtrKwbCwc3ndWdrL_GPC-AeWs33C0RPhDjAMIs7Cf7uZBjUY9TH1-RuD9m5yXOtBAdtkjiTjzJWKAoQWRYNxOVlYzAN31DKk7wavIWJK9wndd2IxLBqRIzhgJYWBFXheCD1XQPZ)** |
| [날씨] 현재 날씨 추천, 이번 주 날씨, ootd<br />[미세먼지] 요일별 추이, 통합대기환경지수 <br />[코로나] 지역별 확진자 추이<br />[자료조사] 각 페이지별 API 조사 | [날씨] 시간대별 기온, ootd<br />[미세먼지] 현재 미세먼지, 지도<br />[코로나] 어제 확진자 수<br />[위치] 좌표 변환, 위경도 주소 변환<br />[서버] aws 서비스 구축, 배포<br />[기타] 팀 리딩, 리팩토링 | [날씨] 시간대별 강수량, 시간대별 api <br />[미세먼지] 시간별 추이 (api 문제로 사라짐)<br />[코로나] 전국 확진자 추이<br />[기타] DB ERD 작성, gitbook 관리 |



# 🌈 프로젝트 구조 및 설명

## ☁ 위치 API

 - 읍면동의 시도, 행정구역코드, 위경도 정보를 반환하는 API
 - 위경도의 읍면동, 행정구역코드 정보를 반환하는 API

## ☁ Weather 페이지

 - 현재 날씨 : 현재 온도, 최고/최저 온도, 날씨 설명, 어제와의 날씨 비교
 - 시간대별 기온 및 강수량(12시간)
 - 기온별 10대~50대 OOTD 추천
 - 이번 주 날씨 

## ☁ Microdust 페이지

 - 현재 미세먼지 농도
 - 요일별 추이
 - 통합대기환경지수
 - 마스크 추천

## ☁ Covid 페이지

 - 현재 코로나 확진자 수
 - 전국 확진자 추이
 - 우리 지역 확진자 추이

    

# 🌈 기술 스택

<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/></a> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/></a>  <img src="https://img.shields.io/badge/Amazon EC2-232F3E?style=flat-square&logo=Amazon%20AWS&logoColor=white"/></a> <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=AmazonS3&logoColor=white"/></a> <img src="https://img.shields.io/badge/GitHub -181717?style=flat-square&logo=GitHub&logoColor=white"/></a> 

![image](https://user-images.githubusercontent.com/68107000/127769260-928490e3-5765-4de3-9c47-95ac4cd55faf.png)


# 🌈 라이브러리

1. lombok
2. spring web
3. spring data jpa
4. spring boot test
5. google code json
6. gson



# 🌈 프로젝트 구조

## ☁ 폴더 구조

1. main/java/com/seeme/SeemeApplication.javamain/java/com/seeme/**controller** ▶️Controller
2. main/java/com/seeme/**service** ▶️ Service
3. main/java/com/seeme/**service**/**api** ▶️ OpenApiService
4. main/java/com/seeme/**domain** ▶️Dto, Entity, Repository
5. main/java/com/seeme/**util** ▶️ const value, static method
6. main/java/com/seeme/**config** ▶️ Config
7. main/resources/application.yml ▶️data source, API url, keytest
8. test/java/com/seeme/SeemeApplicationTests 
9. test/java/com/seeme/**controller** ▶️ integration test



<pre>
    <code>
   ├─main
│  ├─java
│  │  └─com
│  │      └─seeme
│  │          │  SeemeApplication.java
│  │          │
│  │          ├─📒 common 
│  │          │      CORSConfig.java
│  │          │      ApiConfig.java
│  │          │
│  │          ├─📒 controller
│  │          │      CovidController.java
│  │          │      LocationController.java
│  │          │      MicrodustController.java
│  │          │      WeatherController.java
│  │          │
│  │          ├─📒 domain
│  │          │  │  ResDto.java
│  │          │  │
│  │          │  ├─📒 covid
│  │          │  │      Coronic.java
│  │          │  │      CovidDto.java
│  │          │  │      CovidRegionalDto.java
│  │          │  │      CovidRegionalResDto.java
│  │          │  │      CovidResDto.java
│  │          │  │
│  │          │  ├─📒 location
│  │          │  │      Address.java
│  │          │  │      AddressCodeResDto.java
│  │          │  │      AddressRepository.java
│  │          │  │      AddressResDto.java
│  │          │  │      TMAddress.java
│  │          │  │      UmdCodeResDto.java
│  │          │  │
│  │          │  ├─📒 microdust
│  │          │  │      Microdust.java
│  │          │  │      MicrodustDay.java
│  │          │  │      MicrodustDayResDto.java
│  │          │  │      MicrodustMainResDto.java
│  │          │  │      MicrodustMapResDto.java
│  │          │  │      MicrodustMaskResDto.java
│  │          │  │      MicrodustResDto.java
│  │          │  │      MicrodustStation.java
│  │          │  │      MicrodustStationRepository.java
│  │          │  │      MicrodustTime.java
│  │          │  │      MicrodustTimeResDto.java
│  │          │  │      MicrodustTotalResDto.java
│  │          │  │
│  │          │  └─📒 weather
│  │          │          Clothes.java
│  │          │          ClothesRepository.java
│  │          │          ClothesResDto.java
│  │          │          Weather.java
│  │          │          WeatherMain.java
│  │          │          WeatherMainMinMax.java
│  │          │          WeatherMainResDto.java
│  │          │          WeatherOotdResDto.java
│  │          │          WeatherRainResDto.java
│  │          │          WeatherTempResDto.java
│  │          │          WeatherTime.java
│  │          │          WeatherTimeResDto.java
│  │          │          WeatherWeekResDto.java
│  │          │
│  │          ├─📒 service
│  │          │  │  CovidService.java
│  │          │  │  LocationService.java
│  │          │  │  MicrodustService.java
│  │          │  │  WeatherService.java
│  │          │  │
│  │          │  └─📒 api
│  │          │          CovidOpenApi.java
│  │          │          LocationApi.java
│  │          │          MicrodustOpenApi.java
│  │          │          WeatherOpenApi.java
│  │          │
│  │          └─📒 util
│  │                  CovidUtil.java
│  │                  ErrorMessage.java
│  │                  JSONParsingUtil.java
│  │                  LocationUtil.java
│  │                  MicrodustUtil.java
│  │                  WeatherUtil.java
│  │
│  └─resources
│          application.yml
│
└─test
    └─java
        └─com
            └─seeme
                │  SeemeApplicationTests.java
                │
                └─📒 controller
                        CovidControllerTest.java
                        LocationControllerTest.java
                        MicrodustControllerTest.java
                        WeatherControllerTest.java
</code>
</pre>




# 🌈 ERD
![image](https://user-images.githubusercontent.com/80563849/127768089-d264abe8-8d61-4c8c-8c1d-16433497225a.png)




# 🌈 API 문서

### [gitbook link](https://ssk0967.gitbook.io/seeme-api/)
