# spring-shopping

## 기능 목록
- [x] 상품은 상품ID, 상품명, 가격, 이미지URL 을 갖는다
- [x] 상품 조회할 수 있다
- [x] 상품을 삭제할 수 있다
- [x] 상품을 추가할 수 있다
- [x] 상품을 수정할 수 있다
- [ ] 상품 추가 및 수정 시에 유효성 검사를 한다
- [x] 상품 이름은 공백 포함하여 최대 15자리
- [x] 상품 이름에는 `( ), [ ], +, -, &, /, _` 외의 특수 문자 사용이 불가하다
- [ ] 상품 이름에는 비속어를 포함할 수 없다. `PurgoMalum`에서 욕설이 포함되어 있는지 확인한다
- [ ] 잘못된 값이 전달되면 클라이언트가 어떤 부분이 왜 잘못되었는지 인지할 수 있도록 응답을 제공
- [x] 상품 저장은 코틀린 컬렉션에 저장한다


### HTTP 메시지 샘플
#### Request
```aiignore
GET /api/products HTTP/1.1
```

GET /api/products HTTP/1.1
Response

#### Response
```aiignore
HTTP/1.1 200 
Content-Type: application/json

[
  {
    "id": 8146027,
    "name": "아이스 카페 아메리카노 T",
    "price": 4500,
    "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
  }
]
```