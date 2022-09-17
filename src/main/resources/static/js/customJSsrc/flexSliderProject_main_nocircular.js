/* Mainscript Starter */
if (window.getSelection) {
	if (window.getSelection().empty) {  // Chrome
		window.getSelection().empty();
	} else if (window.getSelection().removeAllRanges) {  // Firefox
		window.getSelection().removeAllRanges();
	}
} else if (document.selection) {  // IE?
	document.selection.empty();
}
/* ~Mainscript Starter */


/* Unique Value Filter Method Definition for Array */
Array.prototype.unique = function() {
	return this.filter(function(value, index, self) {
		return self.indexOf(value) === index;
	});
};
/* ~Unique Value Filter Method Definition for Array */


/* Multiple EventListener Mapper on a single element */
// Element.prototype.addMultipleEventsListners = function(el, s, fn) {
	// s.split(' ').forEach(e => el.addEventListener(e, fn, false));
// };
/* ~Multiple EventListener Mapper on a single element */

/* Polyfill for IE */
if (!String.prototype.includes) {
	String.prototype.includes = function(search, start) {
		'use strict';
		if (search instanceof RegExp) {
			throw TypeError('first argument must not be a RegExp');
		} 
		if (start === undefined) { start = 0; }
			return this.indexOf(search, start) !== -1;
	};
}
/* ~Polyfill for IE */



window.addEventListener('load', function() {
	// ending effect of the preloader
	
	setTimeout(function(){
		document.getElementById('flexSlider_preloader').style.visibility = 'hidden';
		document.getElementById('flexSlider_preloader').style.display = 'none';
	}, 2000);

	var currentFocusGrid = {
		currentCategory: 0,
		currentItem: 0
	};
	var coordArray = [];
	var bookHistory = [];
	var indexMapArray = null;
	var arrayForItemsInThisCategory = null;

	function buildFlexSlider(givenDataSet) {
	/*
		givenDataSet = [
        			    {
        			        bookId : '',
        			        comment_brief : [
        			            comment_id : ""
        			        ],
        			        title : '',
        			        url : ''
        			    }
        			];
      */
      console.dir(givenDataSet,'givenDataSet');
		indexMapArray = [];
		// extracting categories sort
		var extractedRawCategories = Array.prototype.slice.call(givenDataSet).map(function(eachElement) {
			return String(eachElement.itemId);
		}).unique();
		console.log(extractedRawCategories,'extractedRawCategories');
		for (var categoriesCounter = 0; categoriesCounter < extractedRawCategories.length; categoriesCounter++) {
			// creating Category Container

			var newCategoryContainer = document.createElement('div');
			newCategoryContainer.classList.add('flexSlider_eachCategory');

			var itemIdElement = document.createElement('input');
			itemIdElement.classList.add('flexSlider_eachBookId');
			itemIdElement.setAttribute("type","hidden");
			itemIdElement.setAttribute("name","flexSlider_eachBookId");
			itemIdElement.setAttribute("value",extractedRawCategories[categoriesCounter]);

			newCategoryContainer.append(itemIdElement);


			var commentArr = givenDataSet[categoriesCounter].recommendCommentList;
			arrayForItemsInThisCategory = [];
			for (var itemCounter = 0; itemCounter < commentArr.length; itemCounter++) {
				/* Rendering the new item */
				var newItemContainerDarkWrapper = document.createElement('div');
				
				var headlineText = document.createTextNode(String(commentArr[itemCounter].content));
				var headlineTextNode = document.createElement('h2');//예정 - p
				headlineTextNode.appendChild(headlineText);
				newItemContainerDarkWrapper.appendChild(headlineTextNode);
				
				var bodyParagraphUpper = document.createTextNode("분류: "+String(givenDataSet[categoriesCounter].title));
				
				var bodyParagraphUpperTextNode = document.createElement('p');
				
				bodyParagraphUpperTextNode.appendChild(bodyParagraphUpper);
				
				newItemContainerDarkWrapper.appendChild(bodyParagraphUpperTextNode);
				
				
				var newItemContainer = document.createElement('a');
				newItemContainer.classList.add('flexSlider_eachItem');
				newItemContainer.href = givenDataSet[categoriesCounter].link;
				// newItemContainer.style.backgroundImage = "url('" + String(itemSetofThisCategory[itemCounter].baseimguri) + "')";
				newItemContainer.style.backgroundColor = "#"+String((Math.random()*0xFFFFFF<<0).toString(16)); // randomly colorizing background
				newItemContainer.appendChild(newItemContainerDarkWrapper);
				
				newCategoryContainer.appendChild(newItemContainer);
				/* ~Rendering the new item */
				
				/* Mapping the new item on the index grid */
				arrayForItemsInThisCategory.push(newItemContainer);
				/* ~Mapping the new item on the index grid */
			}
			
			// mapping the new items into each category, and submitting the complete category on the HTMLbody
			indexMapArray.push(arrayForItemsInThisCategory);
			document.getElementById('slide-section').appendChild(newCategoryContainer);
			
			//첫번째 카테고리만 history로 save
			var bookIdsArray = document.getElementsByClassName('flexSlider_eachBookId');
			//saveHistory(bookIdsArray[0].value);
			bookHistory.push(bookIdsArray[0].value);
		}
		return indexMapArray;
	}
	
	function buildAddFlexSlider(givenDataSet){
		
		// extracting categories sort
		var extractedRawCategories = Array.prototype.slice.call(givenDataSet).map(function(eachElement) {
			return String(eachElement.itemId);
		}).unique();

		for (var categoriesCounter = 0; categoriesCounter < extractedRawCategories.length; categoriesCounter++) {
			// creating Category Container
			var newCategoryContainer = document.createElement('div');
			newCategoryContainer.classList.add('flexSlider_eachCategory');

			var itemIdElement = document.createElement('input');
				itemIdElement.classList.add('flexSlider_eachBookId');
				itemIdElement.setAttribute("type","hidden");
				itemIdElement.setAttribute("name","flexSlider_eachBookId");
				itemIdElement.setAttribute("value",extractedRawCategories[categoriesCounter]);
			
			newCategoryContainer.append(itemIdElement);
			
			var commentArr = givenDataSet[categoriesCounter].recommendCommentList;
			arrayForItemsInThisCategory = [];
			for (var itemCounter = 0; itemCounter < commentArr.length; itemCounter++) {
				/* Rendering the new item */
				var newItemContainerDarkWrapper = document.createElement('div');
	
				var headlineText = document.createTextNode(String(commentArr[itemCounter]));
				var headlineTextNode = document.createElement('h2');//예정 - p
				headlineTextNode.appendChild(headlineText);
				newItemContainerDarkWrapper.appendChild(headlineTextNode);
				
				var bodyParagraphUpper = document.createTextNode("분류: "+String(givenDataSet[categoriesCounter].title));
				
				var bodyParagraphUpperTextNode = document.createElement('p');
				
				bodyParagraphUpperTextNode.appendChild(bodyParagraphUpper);
				
				newItemContainerDarkWrapper.appendChild(bodyParagraphUpperTextNode);
				
				
				var newItemContainer = document.createElement('a');
				newItemContainer.classList.add('flexSlider_eachItem');
				newItemContainer.href = givenDataSet[categoriesCounter].link;
				newItemContainer.style.backgroundColor = "#"+String((Math.random()*0xFFFFFF<<0).toString(16)); // randomly colorizing background
				newItemContainer.appendChild(newItemContainerDarkWrapper);
				
				newCategoryContainer.appendChild(newItemContainer);
				/* ~Rendering the new item */
				
				/* Mapping the new item on the index grid */
				arrayForItemsInThisCategory.push(newItemContainer);
				/* ~Mapping the new item on the index grid */
				console.trace();
			}
			// mapping the new items into each category, and submitting the complete category on the HTMLbody
			indexMapArray.push(arrayForItemsInThisCategory);
			document.getElementById('slide-section').appendChild(newCategoryContainer);
		}
	}
	
	/* Shifting Focus Up */
	function moveFocusUpper() {
		if (currentFocusGrid.currentItem <= 0) {
			setTimeout(function() {
				coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '0px';
				coordArray[currentFocusGrid.currentCategory][coordArray[currentFocusGrid.currentCategory].length-1].style.top = '-100vh';
				coordArray[currentFocusGrid.currentCategory][coordArray[currentFocusGrid.currentCategory].length-1].style.display = "flex";
				setTimeout(function() {
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '100vh';
					coordArray[currentFocusGrid.currentCategory][coordArray[currentFocusGrid.currentCategory].length-1].style.top = '0px';
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.animation = "focusOut-up 1s 1";
					coordArray[currentFocusGrid.currentCategory][coordArray[currentFocusGrid.currentCategory].length-1].style.animation = "focusIn-up 1s 1";
					setTimeout(function() {
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.display = "none";
						setTimeout(function() {
							currentFocusGrid.currentItem = coordArray[currentFocusGrid.currentCategory].length-1;
						}, 0);
					}, 1000);
				}, 10);
			}, 0);
		} else {
			setTimeout(function() {
				coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '0px';
				coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem-1].style.top = '-100vh';
				coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem-1].style.display = "flex";
				setTimeout(function() {
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '100vh';
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem-1].style.top = '0px';
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.animation = "focusOut-up 1s 1";
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem-1].style.animation = "focusIn-up 1s 1";
					setTimeout(function() {
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.display = "none";
						setTimeout(function() {
							currentFocusGrid.currentItem -= 1;
						}, 0);
					}, 1000);
				}, 10);
			}, 0);
		}
	}
	/* ~Shifting Focus Up */

	/* Shifting Focus Down */
	function moveFocusLower() {
		if (currentFocusGrid.currentItem >= coordArray[currentFocusGrid.currentCategory].length-1) {
			setTimeout(function() {
				coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '0px';
				coordArray[currentFocusGrid.currentCategory][0].style.top = '100vh';
				coordArray[currentFocusGrid.currentCategory][0].style.display = "flex";
				setTimeout(function() {
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '-100vh';
					coordArray[currentFocusGrid.currentCategory][0].style.top = '0px';
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.animation = "focusOut-down 1s 1";
					coordArray[currentFocusGrid.currentCategory][0].style.animation = "focusIn-down 1s 1";
					setTimeout(function() {
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.display = "none";
						setTimeout(function() {
							currentFocusGrid.currentItem = 0;
						}, 0);
					}, 1000);
				}, 10);
			}, 0);
		} else {
			setTimeout(function() {
				coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '0px';
				coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem+1].style.top = '100vh';
				coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem+1].style.display = "flex";
				setTimeout(function() {
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '-100vh';
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem+1].style.top = '0px';
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.animation = "focusOut-down 1s 1";
					coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem+1].style.animation = "focusIn-down 1s 1";
					setTimeout(function() {
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.display = "none";
						setTimeout(function() {
							currentFocusGrid.currentItem += 1;
						}, 0);
					}, 1000);
				}, 10);
			}, 0);
		}
	}
	/* ~Shifting Focus Down */

	/* Shifting Focus Left */
	function moveFocusLeftward() {		
		const categoryArray = document.getElementsByClassName('flexSlider_eachCategory');
		const itemsArray = document.getElementsByClassName('flexSlider_eachItem');
		if (currentFocusGrid.currentCategory <= 0) {
            tata.error('카테고리 목록의 처음입니다', 'FlexSlider', {
                position: 'br',
                duration: 800
            });
		} else {
			setTimeout(function() {
				categoryArray[currentFocusGrid.currentCategory].style.left = '0vw';
				categoryArray[currentFocusGrid.currentCategory-1].style.left = '-100vw';
				categoryArray[currentFocusGrid.currentCategory-1].style.display = "flex";
				if (currentFocusGrid.currentItem > coordArray[currentFocusGrid.currentCategory-1].length -1) {
					currentFocusGrid.currentItem = coordArray[currentFocusGrid.currentCategory-1].length -1;
				}
				setTimeout(function() {
					for (var itemCounter = 0; itemCounter < itemsArray.length; itemCounter++) itemsArray[itemCounter].style.display = "none";
					setTimeout(function() {
						coordArray[currentFocusGrid.currentCategory-1][currentFocusGrid.currentItem].style.top = '0';
						coordArray[currentFocusGrid.currentCategory-1][currentFocusGrid.currentItem].style.display = "flex";
						setTimeout(function() {
							categoryArray[currentFocusGrid.currentCategory].style.animation = "focusOut-left 1s 1";
							categoryArray[currentFocusGrid.currentCategory-1].style.animation = "focusIn-left 1s 1";
							setTimeout(function() {
								categoryArray[currentFocusGrid.currentCategory].style.left = '100vw';
								categoryArray[currentFocusGrid.currentCategory-1].style.left = '0vw';
								categoryArray[currentFocusGrid.currentCategory].style.display = "none";
								setTimeout(function() {
									currentFocusGrid.currentCategory -= 1;
								}, 0);
							}, 1000);
						}, 10);
					}, 0);
				}, 0);
			}, 0);
		}
	}
	/* ~Shifting Focus Left */

	/* Shifting Focus Right */
	function moveFocusRightward() {
		var categoryArray = document.getElementsByClassName('flexSlider_eachCategory');
		var itemsArray = document.getElementsByClassName('flexSlider_eachItem');
		var bookIdsArray = document.getElementsByClassName('flexSlider_eachBookId');

		if (currentFocusGrid.currentCategory >= categoryArray.length-1) {
//			addSlider().then(
//				()=>{
//					moveFocusRightward();
//				}
//			);
		} else {
			setTimeout(function() {
				categoryArray[currentFocusGrid.currentCategory].style.left = '0vw';
				categoryArray[currentFocusGrid.currentCategory+1].style.left = '100vw';
				categoryArray[currentFocusGrid.currentCategory+1].style.display = "flex";
				if (currentFocusGrid.currentItem > coordArray[currentFocusGrid.currentCategory+1].length -1) {
					currentFocusGrid.currentItem = coordArray[currentFocusGrid.currentCategory+1].length -1;
				}
				setTimeout(function() {
					for (var itemCounter = 0; itemCounter < itemsArray.length; itemCounter++){
						itemsArray[itemCounter].style.display = "none";
					} 
					setTimeout(function() {
						console.log("coordArray",coordArray);
						console.log("currentFocusGrid.currentCategory",currentFocusGrid.currentCategory);
						console.log("currentFocusGrid.currentItem",currentFocusGrid.currentItem);
						coordArray[currentFocusGrid.currentCategory+1][currentFocusGrid.currentItem].style.top = '0';
						coordArray[currentFocusGrid.currentCategory+1][currentFocusGrid.currentItem].style.display = "flex";
						setTimeout(function() {
							categoryArray[currentFocusGrid.currentCategory].style.animation = "focusOut-right 1s 1";
							categoryArray[currentFocusGrid.currentCategory+1].style.animation = "focusIn-right 1s 1";
							setTimeout(function() {
								categoryArray[currentFocusGrid.currentCategory].style.left = '-100vw';
								categoryArray[currentFocusGrid.currentCategory+1].style.left = '0vw';
								categoryArray[currentFocusGrid.currentCategory].style.display = "none";
								setTimeout(function() {
									//book history insert
									console.log("bookIdsArray",bookIdsArray);
									//saveHistory(bookIdsArray[currentFocusGrid.currentCategory+1].value);
									console.log("currentFocusGrid.currentCategory",currentFocusGrid.currentCategory);
									currentFocusGrid.currentCategory += 1;
								}, 0);
							}, 1000);
						}, 10);
					}, 0);
				}, 0);
			}, 0);
		}
	}
	/* ~Shifting Focus Right */
	
	
	
	// adding EventListener for Clicking Arrow
	document.getElementById("flexSlider_arrow-Upper").addEventListener('click', function() {
		moveFocusUpper();
	});
	document.getElementById("flexSlider_arrow-Lower").addEventListener('click', function() {
		moveFocusLower();
	});
	document.getElementById("flexSlider_arrow-Left").addEventListener('click', function() {
		moveFocusLeftward();
	});
	document.getElementById("flexSlider_arrow-Right").addEventListener('click', function() {
		moveFocusRightward();
	});
	
	
	var swipeEventsContainer = document.getElementById('slide-section');
	var swipeEventslistener = SwipeListener(swipeEventsContainer);
	swipeEventsContainer.addEventListener('swipe', function (e) {
		var directions = e.detail.directions;
		var x = e.detail.x;
		var y = e.detail.y;
		
		if (directions.top && directions.right) {
			moveFocusLower();
		} else if (directions.bottom && directions.right) {
			moveFocusLeftward();
		} else if (directions.bottom && directions.left) {
			moveFocusUpper();
		} else if (directions.top && directions.left) {
			moveFocusRightward();
		} else if (directions.top) {
			moveFocusLower();
		} else if (directions.right) {
			moveFocusLeftward();
		} else if (directions.bottom) {
			moveFocusUpper();
		} else if (directions.left) {
			moveFocusRightward();
		}
	});
	
	
	
	// Request & Response
	var jsonizedGivenDataToLoad = new Object();
	jsonizedGivenDataToLoad.connectKey = "wishket_flexSlider_20200318";
	const jsonizedGivenDataset = JSON.stringify(jsonizedGivenDataToLoad);

	let bkCategory = document.getElementById("bkCategory");
	let bkCategoryValue = bkCategory.options[bkCategory.selectedIndex].value;
	let uriSchemeCMDtoGETformattedDataset = "http://localhost:8080/recommend/list?subCid="+bkCategoryValue;
	const proxyPagetoBypassCORS = "https://cors-anywhere.herokuapp.com/";
	
	const xmlHTTPInstanceforFlexSlider = new XMLHttpRequest();
	xmlHTTPInstanceforFlexSlider.open("POST", uriSchemeCMDtoGETformattedDataset, true);
	// xmlHTTPInstanceforFlexSlider.open("POST", proxyPagetoBypassCORS+uriSchemeCMDtoGETformattedDataset, true);
	xmlHTTPInstanceforFlexSlider.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
	xmlHTTPInstanceforFlexSlider.send(jsonizedGivenDataset);
	
	xmlHTTPInstanceforFlexSlider.onload = function() {
		if (xmlHTTPInstanceforFlexSlider.status === 200) {
			if (xmlHTTPInstanceforFlexSlider.responseText !== '' && xmlHTTPInstanceforFlexSlider.responseText !== null && xmlHTTPInstanceforFlexSlider.responseText !== undefined) {
				// callback
				const themessageBodyParts = JSON.parse(xmlHTTPInstanceforFlexSlider.responseText);
				coordArray = buildFlexSlider(themessageBodyParts);
				coordArray[0][0].style.display = "flex";
				document.getElementsByClassName('flexSlider_eachCategory')[0].style.display = "flex";
				
			} else console.log("연결 오류.");
		} else console.log("Request Denied: Failure Code "+String(xmlHTTPInstanceforFlexSlider.status));
	};
	
	xmlHTTPInstanceforFlexSlider.onerror = function() {
		console.log("서비스 연결 실패.");
	};


	function addSlider(){
		return new Promise((resolve, reject)=>{
			const xmlHTTPInstanceforAddFlexSlider = new XMLHttpRequest();
			xmlHTTPInstanceforAddFlexSlider.open("POST", uriSchemeCMDtoGETformattedDataset, true);
			xmlHTTPInstanceforAddFlexSlider.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
			xmlHTTPInstanceforAddFlexSlider.send(jsonizedGivenDataset);
			xmlHTTPInstanceforAddFlexSlider.onload = function(){
				if(xmlHTTPInstanceforAddFlexSlider.status === 200){
					if (xmlHTTPInstanceforAddFlexSlider.responseText !== '' && xmlHTTPInstanceforAddFlexSlider.responseText !== null && xmlHTTPInstanceforAddFlexSlider.responseText !== undefined) {

						var themessageBodyParts = JSON.parse(xmlHTTPInstanceforAddFlexSlider.responseText);
						buildAddFlexSlider(themessageBodyParts);
						resolve("success");
					}else{
						alert("더 이상 추천해드릴 책이 없습니다.");
						return false;
					}
				} else console.log("Request Denied: Failure Code "+String(xmlHTTPInstanceforAddFlexSlider.status));
			}
		});
	}
	
	function saveHistory(bookId){
		if(!bookHistory.includes(bookId)){
			const xmlHTTPInstanceforAddFlexSlider = new XMLHttpRequest();
			xmlHTTPInstanceforAddFlexSlider.open("GET", "http://localhost:8080/recommend/saveHistory.do?bookId="+bookId,true);
			xmlHTTPInstanceforAddFlexSlider.setRequestHeader("Content-Type", "application/json; charset=UTF-8");			 
			xmlHTTPInstanceforAddFlexSlider.send();
			
			xmlHTTPInstanceforAddFlexSlider.onload = function(){
				if(xmlHTTPInstanceforAddFlexSlider.status === 200){
					if (xmlHTTPInstanceforAddFlexSlider.responseText !== null && xmlHTTPInstanceforAddFlexSlider.responseText !== undefined) {
						console.log("saveHistory success"+bookId);
						bookHistory.push(bookId);
					}else{
						alert("saveHistory success");
						return false;
					}
				} else console.log("Request Denied: Failure Code "+String(xmlHTTPInstanceforAddFlexSlider.status));
			}
		}
	}
});