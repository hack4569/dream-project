class InfiniteScroller {
    summaryDoc;
    collectionCount;
    isJumpingTo = 'none';

    constructor(summaryDoc, collectionCount, isActive) {
        this.summaryDoc = summaryDoc;

        this.collectionCount = this.increaseCollections(collectionCount);
        this.setSectionHeightToSections();
        this.scrollToCenterOfDoc(isActive);
        this.addInfiniteScroll();
        this.addSectionActivation();

        if(isActive) {
            this.initialActivateMiddleSection();
        }
    }

    get scrollTop() {
        return this.summaryDoc.scrollTop;
    }

    get docHeight() {
        return this.summaryDoc.scrollHeight;
    }

    get collectionHeight() {
        const collection = this.summaryDoc.querySelector('.summary-content-collection');

        if(!collection) {
            return -1;
        } else {
            return collection.offsetHeight;
        }
    }

    get sectionHeight() {
        return this.summaryDoc.offsetHeight;
    }

    get sectionCountInCollection() {
        const collection = this.summaryDoc.querySelector('.summary-content-collection');

        if(!collection) {
            return -1;
        } else {
            const sections = collection.querySelectorAll('.summary-content-section');

            if(sections.length === 0) {
                return -1;
            } else {
                return sections.length;
            }
        }
    }

    get sectionCountInDoc() {
        return this.collectionCount * this.sectionCountInCollection;
    }

    setSectionHeightToSections() {
        const sections = this.summaryDoc.querySelectorAll('.summary-content-section');
        const sectionHeight = this.sectionHeight;

        sections.forEach((section) => {
            section.style.height = sectionHeight + "px";
        })
    }

    increaseCollections(collectionCount = 3) {
        // summary-content 안에는 summary-content-collection이 있고,
        // summary-content-collection 안에는 summary-content-section(소개, 인용, 리뷰)들이 들어있습니다.
        // 무한 스크롤 구현을 위해, summary-content-collection을 (collectionCount - 1)만큼 복사해서 붙여넣습니다.
        // 이 경우, 처음 상태는 collection이 collectionCount만큼 있게 됩니다.
        const collections = this.summaryDoc.querySelectorAll('.summary-content-collection');

        if(collections.length === 0) {
            return;
        }

        const source = collections[0];
        for(let i = 0, index = 0; i < (collectionCount - 1); i++, index++) {
            const target = source.cloneNode(true);
            this.summaryDoc.append(target);
        }

        let sectionIndex = 0;
        collections.forEach((collection) => {
            const sections = collection.querySelectorAll('.summary-content-section');
            sections.forEach((section) => {
                section.dataset.index = (sectionIndex).toString();
                sectionIndex++;
            });
        })

        return collectionCount;
    }

    scrollToCenterOfDoc(isActive) {
        const contents = this.summaryDoc.querySelectorAll('.summary-content-section');
        const size = contents.length;
        const middleIndex = Math.floor(size / 2);
        const target = contents[middleIndex];

        this.summaryDoc.scrollTop = target.offsetTop;
        if(isActive) {
            target.classList.add('summary-content-section--active');
        }
    }

    scrollEdgeToEdge(touched, beforeScrollTop, margin) {
        const scrollTop = this.scrollTop;
        const sectionHeight = this.sectionHeight;
        const collectionHeight = this.collectionHeight;
        const docHeight = this.docHeight;

        if(touched) {
            console.log("touch out");
            return [false, scrollTop];
        }
        // beforeScrollTop이 0 이하라는 건, 초기화 상태라는 뜻입니다.
        // 그러므로 scrollTop을 넣습니다.
        // diff는 이전 스크롤 이벤트와 지금 스크롤 이벤트 사이에 스크롤 양입니다.
        // 음수일 경우 밑쪽으로 스크롤한 것이고, 양수일 경우 반대방향입니다.
        // const diff = (beforeScrollTop < 0 ? scrollTop : beforeScrollTop) - scrollTop;
        const diff = beforeScrollTop - scrollTop;

        // diff가 sectionHeight보다 크다는 것은, 한 화면 이상 움직인 것이므로,
        // 점프했다는 뜻입니다.
        // 점프했을 경우 이전 이벤트에서 이미 스크롤 이동을 한 것이므로,
        // 함수를 작동할 필요 없습니다.
        if(Math.abs(diff) > sectionHeight) {
            return [false, scrollTop];
        }

        // console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        // console.log("beforeScrollTop : ", scrollTop);
        // console.log("scrollTop : ", scrollTop);

        // if (scrollTop + collectionHeight > docHeight) {
        // console.log("jumping to : ", this.isJumpingTo);
        // console.log("scrolltop : ", scrollTop);
        // console.log("collectionHeight : ", collectionHeight);
        // console.log("docHeight : ", docHeight);
        // console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        if (scrollTop + collectionHeight > docHeight) {
            if(this.isJumpingTo !== 'top') {
                console.log("Go to top!!");
                this.summaryDoc.scrollTo(0, margin);
                this.isJumpingTo = 'top';
                console.log("scroll To : ", margin);

                return [true, scrollTop];
            }
        // } else if (scrollTop < margin) {
        } else if (scrollTop < collectionHeight) {
            if(this.isJumpingTo !== 'bottom') {
                console.log("scrollTop : ", this.summaryDoc.scrollTop);
                console.log("Go to bottom!!");
                this.summaryDoc.scrollTop = docHeight - collectionHeight - 4;
                console.log("scroll To : ", docHeight - collectionHeight - 4);
                this.isJumpingTo = 'bottom';

                return [true, scrollTop];
            }
        }

        this.isJumpingTo = 'none';

        return [false, scrollTop];
    }

    addInfiniteScroll() {
        let disableScroll = false;
        let touched = false;
        const margin = 4;
        let beforeScrollTop = -1;

        // this.summaryDoc.addEventListener('touchstart', (e) => {
        //     touched = true;
        //     // [disableScroll, beforeScrollTop] = this.scrollEdgeToEdge(touched, beforeScrollTop, margin);
        // })
        // this.summaryDoc.addEventListener('touchmove', (e) => {
        //     touched = true;
        // })
        // this.summaryDoc.addEventListener('touchend', (e) => {
        //     touched = false;
        // })

        this.summaryDoc.addEventListener('scroll',(scrollEvent) => {
            window.requestAnimationFrame((domTimeStamp) => {
                // if(disableScroll) {
                //     return;
                // }

                [disableScroll, beforeScrollTop] = this.scrollEdgeToEdge(touched, beforeScrollTop, margin);

                // if (disableScroll) {
                //     // Disable scroll-jumping for a short time to avoid flickering
                //     window.setTimeout(function () {
                //         disableScroll = false;
                //     }, 1000);
                // }
            })
        });
    }

    activateSection(beforeIndex, beforeScrollTop, threshold) {
        const sections = this.summaryDoc.querySelectorAll('.summary-content-section');
        const activeSection = Array.from(sections).find(section => section.classList.contains('summary-content-section--active'));
        const beforeActiveSection = this.summaryDoc.querySelector('.summary-content-section--before-active');
        const scrollTop = this.scrollTop;
        const sectionCountInDoc = this.sectionCountInDoc;
        let currentIndex = Math.floor(scrollTop / this.sectionHeight);
        let nextIndex = null;
        let jumping = false;
        const diff = beforeScrollTop - scrollTop;

        console.log("beforeScrollTop : ", beforeScrollTop);
        console.log("scrollTop : ", scrollTop);
        console.log("diff : ", diff);
        console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        // if( Math.abs(diff) > this.sectionHeight ) {
        //     jumping = true;
        // }
        if(scrollTop < -4) {
            return [beforeIndex, scrollTop];
        }
        if( Math.abs(diff) > this.sectionHeight ) {
            jumping = true;
        }

        if( diff > 0 ) {
            currentIndex += 1;
        }

        if(!activeSection) {
            return [-1, scrollTop];
        }

        if(Math.abs(diff) < threshold) {
            console.log("diff : ", diff);
            return [beforeIndex, scrollTop];
        }

        if(beforeIndex === currentIndex) {
            return [beforeIndex, scrollTop];
        }

        if(!!beforeActiveSection && beforeActiveSection.classList.contains('summary-content-section--before-active')) {
            beforeActiveSection.classList.remove('summary-content-section--before-active');
        }

        console.log("beforeIndex : ", beforeIndex);
        console.log("currentIndex : ", currentIndex);

        if(!jumping) {
            console.log("next!!");
            if(diff < 0) {
                nextIndex = (currentIndex + 1) % this.sectionCountInDoc;
            } else {
                nextIndex = (currentIndex - 1) < 0 ? (sectionCountInDoc - 1) : currentIndex - 1;
            }
        } else {
            console.log("jumping!!");
            // 점프했으므로 점프 전에 흔적을 지우고,
            // 점프 후에 상황으로 다시 세팅해야합니다.
            // currentIndex가 점프 전의 페이지 인덱스기 때문에, before-active시킵니다.

            if(diff < 0) {
                // 첫번째 콜렉션의 인용에서 마지막 콜렉션의 인용로 점프.
                // 마지막 콜렉션의 첫번째 인덱스로 점프하고, 그 이전 인덱스가 다음 인덱스입니다.
                nextIndex = this.sectionCountInCollection * (this.collectionCount - 1) - 1;
            } else {
                // 마지막 콜렉션의 리뷰에서 첫번째 콜렉션의 리뷰로 점프.
                // 첫번째 콜렉션의 마지막 인덱스로 점프하고, 그 다음 인덱스가 다음 인덱스입니다.
                nextIndex = this.sectionCountInCollection;
            }
        }

        console.log("next index : ", nextIndex);

        activeSection.classList.remove('summary-content-section--active');
        activeSection.classList.remove('summary-content-section--jumped');
        sections[nextIndex].classList.add('summary-content-section--active');

        if(!jumping) {
            activeSection.classList.add('summary-content-section--before-active');
        } else {
            sections[currentIndex].classList.add('summary-content-section--before-active');
            sections[currentIndex].classList.add('summary-content-section--jumped');
        }

        console.log("~~~~~~~~~~~~~~~~~~");

        return [currentIndex, scrollTop];
    }

    initialActivateMiddleSection() {
        const contents = this.summaryDoc.querySelectorAll('.summary-content-section');
        const size = contents.length;
        const middleIndex = Math.floor(size / 2);
        const target = contents[middleIndex];

        target.classList.add('summary-content-section--active');
    }

    addSectionActivation() {
        let beforeScrollTop = this.scrollTop;
        let disableScroll = false;
        let beforeIndex = -1;

        this.summaryDoc.addEventListener('touchstart', (e) => {
            // console.log('touchstart');
            disableScroll = true;
        })
        this.summaryDoc.addEventListener('touchmove', (e) => {
            disableScroll = true;
            // console.log('touchmove');
        })
        this.summaryDoc.addEventListener('touchend', (e) => {
            disableScroll = false;
            // console.log('touchend');
        })
        this.summaryDoc.addEventListener('scroll', (e) => {
            window.requestAnimationFrame((domTimeStamp) => {
                if(disableScroll) {
                    return;
                }
                // [beforeIndex, beforeScrollTop] = this.activateSection(beforeIndex, beforeScrollTop, 10);
            })
        });
    }
}
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

    activateMiddleContentOfBookSummary() {
        const scrollLeft = this.scrollLeft;
        const scrollWidth = this.bookSummariesWidth;
        const bookSummaryElements = this.bookSummaryElements;
        const summaryWidth = this.bookSummaryWidth;

        if (scrollLeft % summaryWidth == 0) {
            const currentIndex = Math.floor(scrollLeft / summaryWidth);
            const currentBookSummary = bookSummaryElements[currentIndex];
            const summaryDoc = currentBookSummary.querySelector('.summary-content');
            const contents = summaryDoc.querySelectorAll('.summary-content-section');
            const middleContent = contents[Math.floor(contents.length / 2)];

            middleContent.classList.add('summary-content-section--active');
        }
    }

    moveToMiddleBookSummary() {
        const size = this.bookSummaryElements.length;
        const middleIndex = Math.floor(size / 2);
        const target = this.bookSummaryElements[middleIndex];

        this.bookSummaries.scrollLeft = target.offsetLeft;
    }
}
((doc) => {
    const bookSummaries = doc.querySelector('.book-summaries');
    const bookSummaryElements = bookSummaries.querySelectorAll('.book-summary');
    const middleIndex = Math.floor(bookSummaryElements.length / 2);
    const collectionCount = 5;

    bookSummaryElements.forEach((bookSummary, index) => {
        const summaryDoc = bookSummary.querySelector('.summary-content');
        // 첫번째 문서부터 시작.
        let isActive = false;
        if(index == 0) {
            isActive = true;
        }

        const scroller = new InfiniteScroller(summaryDoc, collectionCount, isActive);
    });

    bookSummaries.addEventListener('scroll', (e) => {
        const swiper = new Swiper(bookSummaries);
        swiper.activateMiddleContentOfBookSummary();
    });

})(document);

