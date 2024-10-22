// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {
            method: 'DELETE'
        })
            .then(()=>{
                alert('삭제가 완료되었습니다.');
                location.replace('/articles');
            });
    });
}
// HTML 에서 id 를 delete-btn 으로 설정한 엘리먼트를 찾아 클릭 이벤트가 발생하면
// fetch() 메서드를 통해 /api/articles/DELETE 요청을 보내는 역할
// then() 메서드는 fetch() 완료시 연이어 실행되며  alert : 완료 팝업을 띄우고, location.replace: 웹 브라우저 화면을 옮겨줌


// 수정 기능
// 1. id 가 modify-btn 인 엘리먼트 조회
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    // 2. 클릭 이벤트가 감지되면 수정 API 요청
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search); // URLSearchParams 로 쿼리 매개변수 파싱
        let id = params.get('id'); // 현재 URL 의 쿼리 문자열에서 id 값을 가져온다

        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: { // JSON 데이터 보낸다고 명시
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ // 수정할 데이터를 JSON 으로 변환하고 요청 본문에 담음
                title : document.getElementById('title').value, // 제목 입력 필드의 값 가져옴
                content : document.getElementById('content').value,
            })
        })
            .then(()=>{
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`);
            });
    });
}
// id가 modify-btn 인 엘리먼트를 찾고, 클릭 이벤트가 발생하면 id가 title, content 인 엘리먼트 값을 가져와
// fetch() 메서드를 통해 수정 API 로 /api/articles/ PUT 요청을 보냄
// 요청 형식을 headers 에 지정하고, body 에 HTML 에 입력한 데이터를 JSON 형식으로 바꿔 보냄


// 등록 기능 : 등록 버튼을 누르면 입력 칸에 있는 데이터를 가져와 게시글 생성 API 에 글 생성 요청을 보내줌
// 1. id 가 create-btn 인 엘리먼트
const createButton = document.getElementById("create-btn");

if (createButton) {
    // 2. 클릭 이벤트가 감지되면 생성 API 요청
    createButton.addEventListener('click', event => {
        fetch(`/api/articles`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content : document.getElementById('content').value,
            }),
        }).then((response) => {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        });
    });
} // id가 create-btn 인 엘리먼트를 찾아 클릭이벤트가 발생하면
// 엘리먼트 값을 가져와 fetch() 메서트를 통해 생성 API 로 POST 요청