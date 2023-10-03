((doc) => {
    // 헤더에 있는 햄버거 버튼(메뉴 열기)입니다. 누르면 배경이 어두워지면서 메뉴가 열립니다.
    const showMenuButton = doc.querySelector('.global-header__show-menu-button');
    if(!!showMenuButton) {
        showMenuButton.addEventListener('click', (e) => {
            const app = doc.querySelector('.app');
            if(!!app) {
                if(app.classList.contains('app--dimmed') && !app.classList.contains('app--global-nav-open')) {
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
    if(!!bookCategoryButton) {
        bookCategoryButton.addEventListener('click', (e) => {
            e.currentTarget.classList.toggle('book-category-button--open')
        });
    }

    // '추천글' 모달창에서, X 버튼을 누르면 창을 닫습니다.
    const recommendationModalCloseButton = doc.querySelector('.recommendation-modal__close-button');
    if(!!recommendationModalCloseButton) {
        recommendationModalCloseButton.addEventListener('click', (e) => {
            const recommendationModal = doc.querySelector('.recommendation-modal')
            recommendationModal.classList.remove('recommendation-modal--show');
        });
    }
})(document);

((doc) => {
    // 서버에서 받은 데이터를 바탕으로, book-summary들을 복사-붙여넣기합니다.
    const bookSummaries = doc.querySelector('.book-summaries');

    // 코드가 total--load-json에서만 작동되도록 하는 코드로, book-summaries에 book-summaries--load-json 클래스가 있을때만 작동합니다.
    if(!bookSummaries || !bookSummaries.classList.contains('book-summaries--load-json')) {
        return;
    }
    const response = [
        {
            itemId : 17243279,
            link : 'https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=17243279',
            title: '면역',
            recommendCommentList :
                [
                    {
                        content : '특정 질환이 없는데도 아프지 않은 곳이 없고 매일 피곤하며 치매가 생긴 것 같다면 가속노화를 의심해야한다. 노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까? 이 책에 그 답이 실려 있다.노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까?',
                        type: 'description'
                    },
                    {
                        content : '프롬은 가장 능동적으로 자신의 퍼스낼리티 전체를 발달시켜 생산적 방향으로 나아가지 않는 한, 아무리 사랑하려고 노력해도 반드시 실패하기 마련이며, 이웃을 사랑하는 능력이 없는 한, 또한 참된 겸손, 용기, 신념, 훈련이 없는 한...',
                        type: 'phrase'
                    },
                    {
                        content : '노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까? 이 책에 그 답이 실려 있다. 노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까?',
                        type: 'review'
                    }
                ]
        },
        {
            itemId : 17243279,
            link : 'https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=17243279',
            title: '사랑의 기술',
            recommendCommentList :
                [
                    {
                        content : '프롬은 가장 능동적으로 자신의 퍼스낼리티 전체를 발달시켜 생산적 방향으로 나아가지 않는 한, 아무리 사랑하려고 노력해도 반드시 실패하기 마련이며, 이웃을 사랑하는 능력이 없는 한, 또한 참된 겸손, 용기, 신념, 훈련이 없는 한...',
                        type: 'description'
                    },
                    {
                        content : '특정 질환이 없는데도 아프지 않은 곳이 없고 매일 피곤하며 치매가 생긴 것 같다면 가속노화를 의심해야한다. 노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까? 이 책에 그 답이 실려 있다.노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까?',
                        type: 'phrase'
                    },
                    {
                        content : '노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까? 이 책에 그 답이 실려 있다.',
                        type: 'review'
                    }
                ]
        },
        {
            itemId : 17243279,
            link : 'https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=17243279',
            title: '면역',
            recommendCommentList :
                [
                    {
                        content : '면역이란 무엇일까. 지금 이 순간에도 우리 몸속에서는 맹렬한 전투가 벌어지고 있다. 예고 없는 무자비한 침입에 맞선 필사적인 방어와 치밀한 전략. 이 책에 그 답이 실려 있다.노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까?',
                        type: 'description'
                    },
                    {
                        content : '프롬은 가장 능동적으로 자신의 퍼스낼리티 전체를 발달시켜 생산적 방향으로 나아가지 않는 한, 아무리 사랑하려고 노력해도 반드시 실패하기 마련이며, 이웃을 사랑하는 능력이 없는 한, 또한 참된 겸손, 용기, 신념, 훈련이 없는 한...',
                        type: 'phrase'
                    },
                    {
                        content : '노화의 예외가 없지만 같은 나이라도 노화의 속도는 다를 수 있다. 왜그럴까? 이 책에 그 답이 실려 있다.',
                        type: 'review'
                    }
                ]
        },
    ];

    const bookSummarySource = doc.querySelector('.book-summary');
    const colors = ['blue-yellow', 'orange-red', 'blue-green', 'blue', 'violet', 'pitch'];
    const thumbnails = ['book_immune.png', 'book_grow-old-slowly.png', 'book_art-of-love.png'];
    const fieldMap = {
        phrase: 'quote',
        description: 'introduction',
        review: 'review',
    }

    const itemCount = response.length;
    for(let i = 1; i < itemCount; i++) {
        let source = bookSummarySource.cloneNode(true);
        bookSummaries.append(source);
    }

    const bookSummaryItems = doc.querySelectorAll('.book-summary');

    response.forEach((item, index) => {
        const title = item.title;
        const thumbnail = thumbnails[index];
        const color = colors[index];
        const summaryContent = item.recommendCommentList;
        const source = bookSummaryItems[index];
        const about = source.querySelector('.summary-about');

        const bookNameElement = about.querySelector('.summary-about__name');
        const bookCoverElement = about.querySelector('.summary-about__cover');

        bookNameElement.textContent = title;
        // bookCoverElement.src = '/assets/' + thumbnail;
        bookCoverElement.src = 'img/' + thumbnail;

        summaryContent.forEach(section => {
            const fieldType = section.type;
            const fieldName = fieldMap[fieldType];
            const content = section.content;
            const contentElement = source.querySelector('.summary-content-section--' + fieldName + ' .summary-content-section__content');
            contentElement.textContent = content;
        })

        source.classList.add('book-summary--' + color);
        bookSummaries.append(source);
    })

    bookSummaryItems[0].classList.add('book-summary--active');

    const openModalButtons = doc.querySelectorAll('.summary-nav-item__show-button');
    openModalButtons.forEach(button => {
        const recommendationModal = doc.querySelector('.recommendation-modal')
        button.addEventListener('click', (e) => {
            recommendationModal.classList.add('recommendation-modal--show');
        });
    })
})(document);

((doc) => {
    // book-summary의 상하 스와이프를, Swiper라는 라이브러리를 이용해 구현합니다.
    const swiper = new Swiper(".book-summary", {
        direction: 'vertical',
        wrapperClass: 'summary-content',
        slideClass: 'summary-content-section',
        slidesPerView: 1,
        loop: true,
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
        if(this.bookSummaryElements.length <= 0) {
            return -1;
        }

        return this.bookSummaryElements[0].offsetWidth;
    }

    activateBookSummaryWhenScroll() {
        // 좌우스크롤을 구현합니다.
        const scrollLeft = this.scrollLeft;
        const bookSummaryElements = this.bookSummaryElements;
        const summaryWidth = this.bookSummaryWidth;

        if (scrollLeft % summaryWidth == 0) {
            const currentIndex = Math.floor(scrollLeft / summaryWidth);
            const currentBookSummary = bookSummaryElements[currentIndex];

            if(currentBookSummary.classList.contains('book-summary--active')) {
                return;
            }

            currentBookSummary.classList.add('book-summary--active');


            const swiper = currentBookSummary.swiper;
            const activeIndex = swiper.activeIndex;
            const activeSlide = swiper.slides[activeIndex];

            // flikering 방지 코드
            activeSlide.classList.add('summary-content-section--active');

            setTimeout(() => {
                activeSlide.classList.remove('summary-content-section--active');
            }, 1500)

        }
    }
}
((doc) => {
    const bookSummaries = doc.querySelector('.book-summaries');
    bookSummaries.addEventListener('scroll', (e) => {
        const swiper = new BookSummarySwiper(bookSummaries);
        swiper.activateBookSummaryWhenScroll();
    });

})(document);

