(function() {
    let pageButtons = document.querySelectorAll('.page-item__link');

    for(let pageButton of pageButtons) {
        pageButton.addEventListener("click", function(e) {
            e.preventDefault();

            let iframes = document.querySelectorAll('.device-item__frame');
            let src = e.currentTarget.href;

            for(let iframe of iframes) {
                iframe.src = src;
            }
        })
    }
})();