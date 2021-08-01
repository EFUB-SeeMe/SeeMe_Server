# ☀웨더레인저 - 백엔드☀ 



## 백엔드 팀원 소개

| [강민지](https://github.com/nitronium102)                    | [박주은](https://github.com/hoit1302)                        | [서수경](https://github.com/sukyeongs)                       |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **![img](https://lh4.googleusercontent.com/fO4iZwIoHdE2Nz9BPVrkDoHo1f7YcpNnnOrUxKnJx8Z3rBPfMhB9AtCSD3qku0U00GirxfRJfUg58T8VIiEl4CXrYnsJL_-Ryoc6MY-q63szPSU8tEme7rMKQ2EpGY-h095BA-Nc)** | **![img](https://lh6.googleusercontent.com/Db8d2da9sy0oj_HWr5g66ACitq1GcJOiKO3m7W9IaOOGj8o_wo6uiMMcIrqVfMyvg2geD3krcRQn11xtdB7WZOyOsGmdhcGPxjT0weJ0uGGhJRy1qXiOLSNVxNtxge-dh51RLdV4)** | **![img](https://lh3.googleusercontent.com/vFtrKwbCwc3ndWdrL_GPC-AeWs33C0RPhDjAMIs7Cf7uZBjUY9TH1-RuD9m5yXOtBAdtkjiTjzJWKAoQWRYNxOVlYzAN31DKk7wavIWJK9wndd2IxLBqRIzhgJYWBFXheCD1XQPZ)** |
| [날씨] 현재 날씨 추천, 이번 주 날씨, ootd<br />[미세먼지] 요일별 추이, 통합대기환경지수 <br />[코로나] 지역별 확진자 추이<br />[자료조사] 각 페이지별 API 조사 | [날씨] 시간대별 기온, ootd<br />[미세먼지] 현재 미세먼지, 지도<br />[코로나] 어제 확진자 수<br />[위치] 좌표 변환, 위경도 주소 변환<br />[서버] aws의 모든 서비스 활용 및 배포<br />[기타] 팀 리딩, 리팩토링 | [날씨] 시간대별 강수량, 시간대별 api <br />[미세먼지] 시간별 추이 (api 값 문제로 사라짐)<br />[코로나] 전국 확진자 추이<br />[기타] DB ERD 작성, gitbook 관리 |



## 프로젝트 구조 및 설명

### 기능

- 위치 API

  - 읍면동의 시도, 행정구역코드, 위경도 정보를 반환하는 api
  - 위경도의 읍면동, 행정구역코드 정보를 반환하는 api

- weather 페이지

  - 현재 날씨 : 현재 온도, 최고/최저 온도, 날씨 설명, 어제와의 날씨 비교
  - 시간대별 기온 및 강수량(12시간)
  - 기온별 10대~50대 OOTD 추천
  - 이번 주 날씨 

- microdust 페이지

  - 현재 미세먼지 농도
  - 요일별 추이
  - 통합대기환경지수
  - 마스크 추천

- covid 페이지

  - 현재 코로나 확진자 수

  - 전국 확진자 추이

  - 우리 지역 확진자 추이

    

## 기술 스택

[![Spring boot](https://img.shields.io/badge/Springboot-6DB33F?style=round-square&logo=Spring Boot&logoColor=white)](https://spring.io/)</a> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/></a>  <img src="https://img.shields.io/badge/Amazon EC2-232F3E?style=flat-square&logo=Amazon%20AWS&logoColor=white"/></a> <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=AmazonS3&logoColor=white"/></a> <img src="https://img.shields.io/badge/GitHub -181717?style=flat-square&logo=GitHub&logoColor=white"/></a> 



## 라이브러리

1. lombok
2. spring web
3. spring data jpa
4. spring boot test
5. google code json
6. gson



## 프로젝트 구조

### 폴더 구조

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
│  │          ├─common
│  │          │      CORSConfig.java
│  │          │      ApiConfig.java
│  │          │
│  │          ├─controller
│  │          │      CovidController.java
│  │          │      LocationController.java
│  │          │      MicrodustController.java
│  │          │      WeatherController.java
│  │          │
│  │          ├─domain
│  │          │  │  ResDto.java
│  │          │  │
│  │          │  ├─covid
│  │          │  │      Coronic.java
│  │          │  │      CovidDto.java
│  │          │  │      CovidRegionalDto.java
│  │          │  │      CovidRegionalResDto.java
│  │          │  │      CovidResDto.java
│  │          │  │
│  │          │  ├─location
│  │          │  │      Address.java
│  │          │  │      AddressCodeResDto.java
│  │          │  │      AddressRepository.java
│  │          │  │      AddressResDto.java
│  │          │  │      TMAddress.java
│  │          │  │      UmdCodeResDto.java
│  │          │  │
│  │          │  ├─microdust
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
│  │          │  └─weather
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
│  │          ├─service
│  │          │  │  CovidService.java
│  │          │  │  LocationService.java
│  │          │  │  MicrodustService.java
│  │          │  │  WeatherService.java
│  │          │  │
│  │          │  └─api
│  │          │          CovidOpenApi.java
│  │          │          LocationApi.java
│  │          │          MicrodustOpenApi.java
│  │          │          WeatherOpenApi.java
│  │          │
│  │          └─util
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
                └─controller
                        CovidControllerTest.java
                        LocationControllerTest.java
                        MicrodustControllerTest.java
                        WeatherControllerTest.java
</code>
</pre>

## ERD

![img](https://lh6.googleusercontent.com/ZP55kR8IXG16yhJKRJPfzXQko1Qg5817NuIjkQL58bWwq-kKp37HeBe-xApRLmcPFyxH0UU7AU7HlDM_qwDKKjAmWZlEvruYErU0o3VemNH5_93zoI2agTPH5aXY6B7RZdExdBIX)



## [API 명세서]([**https://ssk0967.gitbook.io/seeme-api/**](https://ssk0967.gitbook.io/seeme-api/))

링크 참고