((doc) => {
    // 헤더에 있는 햄버거 버튼(메뉴 열기)입니다. 누르면 배경이 어두워지면서 메뉴가 열립니다.
    const showMenuButton = doc.querySelector('.global-header__show-menu-button');
    if (!!showMenuButton) {
        showMenuButton.addEventListener('click', (e) => {
            const app = doc.querySelector('.app');
            if (!!app) {
                if (app.classList.contains('app--dimmed') && !app.classList.contains('app--global-nav-open')) {
                    return;
                }
                app.classList.toggle('app--dimmed');
                app.classList.toggle('app--global-nav-open');
            }
        });
    }

    // icon-button 클래스가 있는 엘리먼트들은 공통적으로 토글기능을 넣어서, 아이콘이 바뀌도록 합니다.
    const iconButtons = doc.querySelectorAll('.icon-button');
    iconButtons.forEach((iconButton) => {
        iconButton.addEventListener('click', (e) => {
            e.currentTarget.classList.toggle('icon-button--on');
        });
    });

    // 버튼을 누르면 '자세한 추천글' 모달창이 뜨도록 합니다.
    const bookCategoryButton = doc.querySelector('.book-category-button');
    if (!!bookCategoryButton) {
        bookCategoryButton.addEventListener('click', (e) => {
            e.currentTarget.classList.toggle('book-category-button--open')
        });
    }

    // '추천글' 모달창에서, X 버튼을 누르면 창을 닫습니다.
    const recommendationModalCloseButton = doc.querySelector('.recommendation-modal__close-button');
    if (!!recommendationModalCloseButton) {
        recommendationModalCloseButton.addEventListener('click', (e) => {
            const recommendationModal = doc.querySelector('.recommendation-modal')
            recommendationModal.classList.remove('recommendation-modal--show');
        });
    }

    const popupInfo1 = document.getElementById('popupInfo1');
    const popupInfo2 = document.getElementById('popupInfo2');
    const dontShowPopupToday = 'dontShowPopupToday';
    const dontShowPopupForever = 'dontShowPopupForever';
    const closeDayChkBox = document.querySelector(".closeDay");
    const closeForeverChkBox = document.querySelector(".closeForever");

    const dontShowTodayCheckbox = document.getElementById('dont-show-today');

    // 오늘 하루 보지 않기 체크 확인
    checkPopup(dontShowPopupToday);
    // 더이상 팝업 보지 않기
    checkPopup(dontShowPopupForever);

    function checkPopup(popupTag) {
        if (localStorage.getItem(popupTag)) {
            let list = localStorage.getItem(popupTag);
            let popupList = list.split(",");
            for (let popupId of popupList) {
                if (popupTag === dontShowPopupToday) {
                    const storedExpireDate = localStorage.getItem("dontShowPopupTodayTime");
                    if (checkOverDay(storedExpireDate)) {
                        localStorage.removeItem('dontShowPopupTodayTime');
                        localStorage.removeItem(dontShowPopupToday);// 하루가 지나면 쿠키 삭제
                    } else {
                        $("#" + popupId).hide();
                    }
                } else if (popupTag === dontShowPopupForever) {
                    if (popupTag === dontShowPopupForever) {
                        $("#" + popupId).hide();
                    }
                }
            }
        }
    }

    if (closeDayChkBox) {
        // 닫기 버튼 클릭 시 팝업 닫기
        closeDayChkBox.addEventListener('click', function(e) {
            e.preventDefault();
            // '오늘 하루 보지 않기'가 체크되었을 경우 Local Storage에 기록
            if (closeDayChkBox.checked) {
                const now = new Date();
                const expireDate = now.setHours(23, 59, 59, 999); // 오늘 자정까지 유효
                localStorage.setItem('dontShowPopupTodayTime', expireDate);
                const popupId = $(this).closest("dialog").prop("id");
                localStorage.setItem(dontShowPopupToday,popupId);
            } else {
                localStorage.setItem(dontShowPopupToday, "");
            }
        });
    }

    if (closeForeverChkBox) {
        closeForeverChkBox.addEventListener('click', function(){
            // '오늘 하루 보지 않기'가 체크되었을 경우 Local Storage에 기록
            if (closeForeverChkBox.checked) {
                const popupId = $(this).closest("dialog").prop("id");
                localStorage.setItem(dontShowPopupForever,popupId);
            } else {
                localStorage.setItem(dontShowPopupForever, "");
            }
        });
    }

    function checkOverDay(storedExpireDate) {
        return storedExpireDate && new Date() > new Date(parseInt(storedExpireDate));
    }
})(document);

//메인 슬라이드 전체적인 구조
((doc) => {
    // 서버에서 받은 데이터를 바탕으로, book-summary들을 복사-붙여넣기합니다.
    const bookSummaries = doc.querySelector('.book-summaries');

    const fieldMap = {
        phrase: 'phrase',
        description: 'description',
        review: 'review',
    }
    const colors = ['blue-yellow', 'orange-red', 'blue-green', 'blue', 'violet', 'pitch'];
    const books = document.querySelectorAll(".book-summary");
    books.forEach((book, index)=>{
        index ++;
        index = index % colors.length;
        const color = colors[index];
        book.classList.add('book-summary--' + color);
    });

    books[0]?.classList.add('book-summary--active');

    const categoryName = document.querySelector(".book-summary--active input[name=categoryName]")?.value;
    document.querySelector(".global-header__page-title").textContent = categoryName;

    const openModalButtons = doc.querySelectorAll('.summary-nav-item__show-button');
    openModalButtons.forEach(button => {
        const recommendationModal = doc.querySelector('.recommendation-modal')
        button.addEventListener('click', (e) => {
            recommendationModal.classList.add('recommendation-modal--show');
        });
    })
})(document);

let historyItemIds = [];

((doc) => {
    // book-summary의 상하 스와이프를, Swiper라는 라이브러리를 이용해 구현합니다.
    const swiper = new Swiper(".book-summary", {
        direction: 'vertical',
        wrapperClass: 'summary-content',
        slideClass: 'summary-content-section',
        slidesPerView: 1,
        loop: false,
        on: {
            slideChangeTransitionStart: (swiper) => {
                // book-summary는 좌우로 스와이프하여 출현하기 전에는 투명상태지만,
                // 좌우로 한 번 이상 스와이프 한 뒤에는 보이기 상태입니다.
                // 이 상황에서 좌우로 스와이프하는 경우, CSS 애니메이션이 재생되면서
                // 상태가 '보임 - 애니메이션 재생(fade in left)'로 진행됩니다.
                // 따라서 flikering 상황이 발생하기 때문에, 클래스를 추가하여 그것을 막습니다.
                const activeIndex = swiper.activeIndex;
                const activeSlide = swiper.slides[activeIndex];

                activeSlide.classList.add('summary-content-section--active');

                saveHistory();

                setTimeout(() => {
                    activeSlide.classList.remove('summary-content-section--active');
                }, 1500)
            },
        }
    });
})(document);

class BookSummarySwiper {
    bookSummaries;

    constructor(bookSummaries) {
        this.bookSummaries = bookSummaries;
    }

    get scrollLeft() {
        return this.bookSummaries.scrollLeft;
    }

    get bookSummariesWidth() {
        return this.bookSummaries.scrollWidth;
    }

    get bookSummaryElements() {
        return this.bookSummaries.querySelectorAll('.book-summary');
    }

    get bookSummaryWidth() {
        if (this.bookSummaryElements.length <= 0) {
            return -1;
        }

        return this.bookSummaryElements[0].offsetWidth;
    }

    // 좌우스크롤을 구현합니다.
    activateBookSummaryWhenScroll() {
        //console.log('left right slide action');
        const scrollLeft = this.scrollLeft;
        const bookSummaryElements = this.bookSummaryElements;
        const summaryWidth = this.bookSummaryWidth;
        //console.dir(bookSummaryElements, 'bookSummaryElements');
        //console.log(summaryWidth, 'summaryWidth');
        //console.log(scrollLeft, 'scrollLeft');

        if ((scrollLeft.toFixed(0) % summaryWidth.toFixed(0)) == 0) {
            const currentIndex = Math.floor(scrollLeft / summaryWidth);
            //console.log(currentIndex, 'currentIndex');
            const currentBookSummary = bookSummaryElements[currentIndex];
            //console.log(currentBookSummary.classList, 'currentBookSummary.classList');
            if (currentBookSummary.classList.contains('book-summary--active')) {
                //console.log('contain book-summary--active');
                return;
            }

            //전체 삭제
            bookSummaryElements.forEach((item)=>{
                //console.log(item.classList, '삭제 item.classList');
               item.classList.remove('book-summary--active');
            });

            currentBookSummary.classList.add('book-summary--active');
            const categoryName = document.querySelector(".book-summary--active input[name=categoryName]")?.value;
            document.querySelector(".global-header__page-title").textContent = categoryName;

            const swiper = currentBookSummary.swiper;
            const activeIndex = swiper.activeIndex;
            const activeSlide = swiper.slides[activeIndex];

            // flikering 방지 코드
            activeSlide.classList.add('summary-content-section--active');

            setTimeout(() => {
                activeSlide.classList.remove('summary-content-section--active');
            }, 1500)

            //책정보 저장
            saveHistory();
        }
    }
}

//책추천 스와이퍼 함수 호출
((doc) => {
    const bookSummaries = doc.querySelector('.book-summaries');
    bookSummaries.addEventListener('scroll', (e) => {
        const swiper = new BookSummarySwiper(bookSummaries);
        swiper.activateBookSummaryWhenScroll();
    });

})(document);

//공통함수
((doc) => {
    //모달 닫기
    // const modalClose = document.querySelector(".guide-dialog__close-button");
    // modalClose.addEventListener("click", (e)=>{
    //     //모달 창 닫기
    //     e.currentTarget.parentElement.style.display = "none";
    //
    //     //배경색 복원
    //     document.querySelector('.app').classList.remove("app--dimmed");
    // });

})(document);

//공통함수 전역으로 사용
function saveHistory() {
    const itemId = document.querySelector(".book-summary--active input[name=itemId]")?.value;
    if (itemId && !historyItemIds.includes(itemId)) {
        $.ajax({
            url: "/recommend/history",
            type: "POST",
            contentType: "application/json",  // JSON 형식으로 데이터 전송
            data: JSON.stringify(parseInt(itemId)),     // itemId를 JSON 형식으로 직렬화
            success: (response) => {
                historyItemIds.push(itemId);
            },
            error: (error) => {
                console.log(error, "saveHistory : error");
            }
        });
    }
}





