//다음 주소검색창
    document.getElementById('searchAddressBtn').addEventListener('click', function() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색 결과를 주소 입력 필드에 설정
                document.getElementById('galAddress').value = data.address;
            }
        }).open();
    });
    
    
