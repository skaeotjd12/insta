/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

//(0)현재 로긴한 사용자 아이디
let principalId = $("#principalId").val();

// (1) 스토리 로드하기
let page = 0;
function storyLoad() {
$.ajax({
	/*getmapping type 디폴타가 get이라 type 안 적어도됨*/
	url:`/api/image?page=${page}`,
	dataType:"json"
}).done(res=>{
	console.log(res);
	res.data.content.forEach((image)=>{
		let storyItem = getStoryItem(image);
		$("#storyList").append(storyItem);
	});
}).fail(error=>{
	console.log("오류",error);
});
}

storyLoad();

function getStoryItem(image) {
	let item = `<div class="story-list__item">
	<div class="sl__item__header">
		<div>
			<img class="profile-image" src="/upload/${image.user.profileImageUrl}"
				onerror="this.src='/images/person.jpeg'" />
		</div>
		<div>${image.user.username}</div>
</div>

<div class="sl__item__img">
	<img src="/upload/${image.postImageUrl}" />
</div>

<div class="sl__item__contents">
	<div class="sl__item__contents__icon">

		<button>`;
			
			if(image.likeState){
				item += `<i class="fas fa-heart active" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
			}else{
				item += `<i class="far fa-heart" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
			}
			
         item += `
		</button>
	</div>

	<span class="like"><b id="storyLikeCount-${image.id}"> ${image.likeCount}</b>likes</span>
	
	<div class="sl__item__contents__content">
		<p>${image.caption}</p>
	</div>

	<div id="storyCommentList-${image.id}">`;

		image.comments.forEach((comment)=>{
			item += `<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
			<p>
				<b>${comment.user.username} : </b> ${comment.content}
			</p>`;
			
			if(principalId == comment.user.id){
				item += `<button onclick="deleteComment(${comment.id})">
									<i class="fas fa-times"></i>
								</button>`;
			}

			
			
			item +=`
		</div>`;
			
		});
		
		
		item += `
	
	</div>

	<div class="sl__item__input">
		<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />
		<button type="button" onClick="addComment(${image.id})">게시</button>
		</div>

	</div>
</div>`;
		
		return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
	//console.log("윈도우 scroll Top", $(window).scrollTop());
	//console.log("문서의 높이", $(document).height());
	//console.log("윈도우 높이", $(window).height());
	// 문서의 높이 - 윈도우 높이 = 스크롤 탑이랑 같아졋을때가 화면크기
	
	let checkNum = $(window).scrollTop() - ( $(document).height() - $(window).height() );
	console.log(checkNum);
	
	if(checkNum < 1 && checkNum > -1) {
		page++;
		storyLoad();
	}
});


// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
	let likeIcon = $(`#storyLikeIcon-${imageId}`);
	if (likeIcon.hasClass("far")) { // 좋아요 하겠다
		
		$.ajax({
			type:"post",
			url:`/api/image/${imageId}/likes`,
			dataType:"json"
		}).done(res => {
			//좋아요 시 화면 좋아요 수 텍스트 값 변동 처리
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) +1;
			console.log("좋아요 카운트 불러오기", likeCount);	
			$(`#storyLikeCount-${imageId}`).text(likeCount);
			//좋아요 시 화면 하트 변동 처리 
			likeIcon.addClass("fas");
			likeIcon.addClass("active");
			likeIcon.removeClass("far");
		}).fail(error =>{
			console.log("오류", error);
		});
		
	} else { // 좋아요 취소 하겠다
			$.ajax({
			type:"delete",
			url:`/api/image/${imageId}/likes`,
			dataType:"json"
		}).done(res => {
			//좋아요 취소 시 화면 좋아요 수 텍스트 값 변동 처리
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) -1;
			$(`#storyLikeCount-${imageId}`).text(likeCount);
			//좋아요 취소 시 실제 화면 하트 변동 처리 
			likeIcon.removeClass("fas");
			likeIcon.removeClass("active");
			likeIcon.addClass("far");
		}).fail(error =>{
			console.log("오류", error);
		});
	}
}

// (4) 댓글쓰기
function addComment(imageId) {

	let commentInput = $(`#storyCommentInput-${imageId}`);
	let commentList = $(`#storyCommentList-${imageId}`);

	let data = {
		imageId,
		content: commentInput.val()
	}

	if (data.content === "") { //데이터 content 프론트에서 미리 막아줌  null 값이면 얼럿후 리턴
		alert("댓글을 작성해주세요!");
		return;
	}
	
	$.ajax({
		type : "post",
		url : "/api/comment",
		data : JSON.stringify(data),
		contentType : "application/json; charset-utf-8",
		dataType : "json"
	}).done(res=>{
		//console.log("성공", res);
		
		let comment = res.data;
		
		let content = `
		  <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}"> 
		    <p>
		      <b>${comment.user.username}</b>
			 ${comment.content}
		    </p>
			    <button onclick="deleteComment(${comment.id})">
			    	<i class="fas fa-times"></i>
			    </button>
		  </div>
			`;
		commentList.prepend(content);
	}).fail(error=>{
		//댓글 널값 유효성검사 서버단에서 검사한 부분임 commetDto에서 notblank 처리
		console.log("오류", error.responseJSON.data.content);
		alert(error.responseJSON.data.content);
	});
	commentInput.val(""); //인풋 필드를 꺠끗하게 비워준다. 오류가 나도 비워줄거라 done에 안넣고 밑에 따로널어둔거임
}

// (5) 댓글 삭제
function deleteComment(commentId) {
	$.ajax({
		type : "delete",
		url: `/api/comment/${commentId}`,
		dataType:"json"
	}).done(res=>{
		console.log("성공", res);
		//댓글 삭제버튼 x누르면 바로 사라지게끔 만드는 것 리무브
		$(`#storyCommentItem-${commentId}`).remove();
	}).fail(error=>{
		console.log("실패", error);
	});
}







