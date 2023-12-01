// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); //폼태그 액션을 막기!
	
	let data = $("#profileUpdate").serialize(); //key=value 형태일때 이렇게받아옴 이런건 프로필 js처럼 formData 형식을 못 만듬 사진을 전송하는게 아니기때문에 사진이 아닌 경우 serialize쓰면됨 
	console.log(data);
	$.ajax({
		type:"put",
		url: `/api/user/${userId}`,
		data: data,
		contentType:"application/x-www-form-urlencoded; charset=urf-8",	
		dataType :"json"		
	}).done(res=>{//HttpStatus 상태코드 200번대 
		console.log("update 성공", res)
		location.href=`/user/${userId}`;
	}).fail(error=>{//HttpStatus 상태코드 200번대가 아닐때
		if(error.data == null) {
			alert(error.responseJSON.message)
		}else{
			alert(JSON.stringify(error.responseJSON.data))
		}
		console.log("update 실패",error.responseJSON.data)		
	})
	};