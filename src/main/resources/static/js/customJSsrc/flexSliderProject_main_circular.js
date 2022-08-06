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
	setTimeout(function() {
		// screen presentation effect
		document.getElementById('flexSlider_preloader').style.animation = "preloaderRemoval 2s 1";
		
		setTimeout(function() {
			// setting the actual rendered body visible
			document.getElementById('flexSlider_preloader').style.visibility = 'hidden';
			document.getElementById('flexSlider_preloader').style.display = 'none';	
		}, 2000);
	}, 2000);
	
	var currentFocusGrid = {
		currentCategory: 0,
		currentItem: 0
	};
	var coordArray = [];
	
	function buildFlexSlider(givenDataSet) {
		// creating index grid mapper 2nd-dimensional array
		var indexMapArray = [];
		
		// extracting categories sort
		var extractedRawCategories = Array.prototype.slice.call(givenDataSet).map(function(eachElement) {
			return String(eachElement.category_id);
		}).unique();
		console.log("extractedRawCategories",extractedRawCategories);
		for (var categoriesCounter = 0; categoriesCounter < extractedRawCategories.length; categoriesCounter++) {
			// creating Category Container
			var newCategoryContainer = document.createElement('div');
			newCategoryContainer.classList.add('flexSlider_eachCategory');
			
			// extracting each item following the category & mapping the index on the grid mapper array
			//var itemSetofThisCategory = givenDataSet.filter(function(eachItem) {
			//	return eachItem.includes(extractedRawCategories[categoriesCounter]);
			//});
			//console.log("test", itemSetofThisCategory);
			//itemSetofThisCategory.sort(function (former, latter) { return former.title - latter.title; });
			var commentArr = givenDataSet[categoriesCounter].comment_brief;
			var arrayForItemsInThisCategory = [];
			for (var itemCounter = 0; itemCounter < commentArr.length; itemCounter++) {
				/* Rendering the new item */
				var newItemContainerDarkWrapper = document.createElement('div');
				
				var headlineText = document.createTextNode(String(commentArr[itemCounter]));
				var headlineTextNode = document.createElement('h2');//예정 - p
				headlineTextNode.appendChild(headlineText);
				newItemContainerDarkWrapper.appendChild(headlineTextNode);
				
				/* Not required paragraph handler should be commented out.
				var bodyParagraph = document.createTextNode(String(itemSetofThisCategory[itemCounter].contextString));
				var bodyParagraphTextNode = document.createElement('p');
				bodyParagraphTextNode.appendChild(bodyParagraph);
				newItemContainerDarkWrapper.appendChild(bodyParagraphTextNode); */
				var bodyParagraphUpper = document.createTextNode("분류: "+String(givenDataSet[categoriesCounter].title));
				
				var bodyParagraphUpperTextNode = document.createElement('p');
				
				bodyParagraphUpperTextNode.appendChild(bodyParagraphUpper);
				
				newItemContainerDarkWrapper.appendChild(bodyParagraphUpperTextNode);
				
				
				var newItemContainer = document.createElement('a');
				newItemContainer.classList.add('flexSlider_eachItem');
				newItemContainer.href = givenDataSet[categoriesCounter].url;
				console.dir(newItemContainer);
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
			document.getElementsByTagName('body')[0].appendChild(newCategoryContainer);
		}
		
		return indexMapArray;
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
			setTimeout(function() {
				categoryArray[currentFocusGrid.currentCategory].style.top = categoryArray[categoryArray.length-1].style.top = '0px';
				categoryArray[currentFocusGrid.currentCategory].style.left = '0px';
				categoryArray[categoryArray.length-1].style.left = '-100vw';
				categoryArray[categoryArray.length-1].style.display = "flex";
				setTimeout(function() {
					for (var itemCounter = 0; itemCounter < itemsArray.length; itemCounter++) itemsArray[itemCounter].style.display = "none";
					setTimeout(function() {
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '0px';
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.left = '0px';
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.display = 'flex';
						setTimeout(function() {
							if (currentFocusGrid.currentItem > coordArray[categoryArray.length-1].length -1) {
								currentFocusGrid.currentItem = coordArray[categoryArray.length-1].length -1;
								coordArray[categoryArray.length-1][coordArray[categoryArray.length-1].length -1].style.top = '0px';
								coordArray[categoryArray.length-1][coordArray[categoryArray.length-1].length -1].style.left = '0px';
								coordArray[categoryArray.length-1][coordArray[categoryArray.length-1].length -1].style.display = 'flex';
							} else {
								coordArray[categoryArray.length-1][currentFocusGrid.currentItem].style.top = '0px';
								coordArray[categoryArray.length-1][currentFocusGrid.currentItem].style.left = '0px';
								coordArray[categoryArray.length-1][currentFocusGrid.currentItem].style.display = "flex";
							}
							setTimeout(function() {
								categoryArray[currentFocusGrid.currentCategory].style.left = '100vw';
								categoryArray[categoryArray.length-1].style.left = '0px';
								categoryArray[currentFocusGrid.currentCategory].style.animation = "focusOut-left 1s 1";
								categoryArray[categoryArray.length-1].style.animation = "focusIn-left 1s 1";
								setTimeout(function() {
									categoryArray[currentFocusGrid.currentCategory].style.display = "none";
									categoryArray[currentFocusGrid.currentCategory].style.animation = "none";
									categoryArray[categoryArray.length-1].style.animation = "none";
									setTimeout(function() {
										currentFocusGrid.currentCategory = categoryArray.length-1;
									}, 0);
								}, 1000);
							}, 0);
						}, 0);
					}, 0);
				}, 0);
			}, 0);
			
		} else {
			setTimeout(function() {
				categoryArray[currentFocusGrid.currentCategory].style.top = categoryArray[currentFocusGrid.currentCategory-1].style.top = '0px';
				categoryArray[currentFocusGrid.currentCategory].style.left = '0px';
				categoryArray[currentFocusGrid.currentCategory-1].style.left = '-100vw';
				categoryArray[currentFocusGrid.currentCategory-1].style.display = "flex";
				setTimeout(function() {
					for (var itemCounter = 0; itemCounter < itemsArray.length; itemCounter++) itemsArray[itemCounter].style.display = "none";
					setTimeout(function() {
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '0px';
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.left = '0px';
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.display = 'flex';
						setTimeout(function() {
							if (currentFocusGrid.currentItem > coordArray[currentFocusGrid.currentCategory-1].length -1) {
								currentFocusGrid.currentItem = coordArray[currentFocusGrid.currentCategory-1].length -1;
								coordArray[currentFocusGrid.currentCategory-1][coordArray[currentFocusGrid.currentCategory-1].length -1].style.top = '0px';
								coordArray[currentFocusGrid.currentCategory-1][coordArray[currentFocusGrid.currentCategory-1].length -1].style.left = '0px';
								coordArray[currentFocusGrid.currentCategory-1][coordArray[currentFocusGrid.currentCategory-1].length -1].style.display = 'flex';
							} else {
								coordArray[currentFocusGrid.currentCategory-1][currentFocusGrid.currentItem].style.top = '0px';
								coordArray[currentFocusGrid.currentCategory-1][currentFocusGrid.currentItem].style.left = '0px';
								coordArray[currentFocusGrid.currentCategory-1][currentFocusGrid.currentItem].style.display = "flex";
							}
							setTimeout(function() {
								categoryArray[currentFocusGrid.currentCategory].style.left = '100vw';
								categoryArray[currentFocusGrid.currentCategory-1].style.left = '0px';
								categoryArray[currentFocusGrid.currentCategory].style.animation = "focusOut-left 1s 1";
								categoryArray[currentFocusGrid.currentCategory-1].style.animation = "focusIn-left 1s 1";
								setTimeout(function() {
									categoryArray[currentFocusGrid.currentCategory].style.display = "none";
									categoryArray[currentFocusGrid.currentCategory].style.animation = "none";
									categoryArray[currentFocusGrid.currentCategory-1].style.animation = "none";
									setTimeout(function() {
										currentFocusGrid.currentCategory -= 1;
									}, 0);
								}, 1000);
							}, 0);							
						}, 0);
					}, 0);
				}, 0);
			}, 0);
		}
	}
	/* ~Shifting Focus Left */

	/* Shifting Focus Right */
	function moveFocusRightward() {
		const categoryArray = document.getElementsByClassName('flexSlider_eachCategory');
		const itemsArray = document.getElementsByClassName('flexSlider_eachItem');
		if (currentFocusGrid.currentCategory >= categoryArray.length-1) {			
			setTimeout(function() {
				console.log("8");
				categoryArray[currentFocusGrid.currentCategory].style.top = categoryArray[0].style.top = '0px';
				categoryArray[currentFocusGrid.currentCategory].style.left = '0px';
				categoryArray[0].style.left = '100vw';
				categoryArray[0].style.display = "flex";
				setTimeout(function() {
					console.log("7");
					for (var itemCounter = 0; itemCounter < itemsArray.length; itemCounter++) itemsArray[itemCounter].style.display = "none";
					setTimeout(function() {
						console.log("6");
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '0px';
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.left = '0px';
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.display = 'flex';
						setTimeout(function() {
							if (currentFocusGrid.currentItem > coordArray[0].length -1) {
								console.log("5");
								currentFocusGrid.currentItem = coordArray[0].length -1;
								coordArray[0][coordArray[0].length -1].style.top = '0px';
								coordArray[0][coordArray[0].length -1].style.left = '0px';
								coordArray[0][coordArray[0].length -1].style.display = 'flex';
							} else {
								console.log("4");
								coordArray[0][currentFocusGrid.currentItem].style.top = '0px';
								coordArray[0][currentFocusGrid.currentItem].style.left = '0px';
								coordArray[0][currentFocusGrid.currentItem].style.display = "flex";
							}
							setTimeout(function() {
								console.log("3");
								categoryArray[currentFocusGrid.currentCategory].style.left = '-100vw';
								categoryArray[0].style.left = '0px';
								categoryArray[currentFocusGrid.currentCategory].style.animation = "focusOut-right 1s 1";
								categoryArray[0].style.animation = "focusIn-right 1s 1";
								setTimeout(function() {
									console.log("2");
									categoryArray[currentFocusGrid.currentCategory].style.display = "none";
									categoryArray[currentFocusGrid.currentCategory].style.animation = "none";
									categoryArray[0].style.animation = "none";
									setTimeout(function() {
										console.log("1");
										currentFocusGrid.currentCategory = 0;
									}, 0);
								}, 1000);
							}, 0);							
						}, 0);
					}, 0);
				}, 0);
			}, 0);
		} else {
			setTimeout(function() {
				categoryArray[currentFocusGrid.currentCategory].style.top = categoryArray[currentFocusGrid.currentCategory+1].style.top = '0px';
				categoryArray[currentFocusGrid.currentCategory].style.left = '0px';
				categoryArray[currentFocusGrid.currentCategory+1].style.left = '100vw';
				categoryArray[currentFocusGrid.currentCategory+1].style.display = "flex";
				setTimeout(function() {
					for (var itemCounter = 0; itemCounter < itemsArray.length; itemCounter++) itemsArray[itemCounter].style.display = "none";
					setTimeout(function() {
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.top = '0px';
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.left = '0px';
						coordArray[currentFocusGrid.currentCategory][currentFocusGrid.currentItem].style.display = 'flex';
						setTimeout(function() {
							if (currentFocusGrid.currentItem > coordArray[currentFocusGrid.currentCategory+1].length -1) {
								currentFocusGrid.currentItem = coordArray[currentFocusGrid.currentCategory+1].length -1;
								coordArray[currentFocusGrid.currentCategory+1][coordArray[currentFocusGrid.currentCategory+1].length -1].style.top = '0';
								coordArray[currentFocusGrid.currentCategory+1][coordArray[currentFocusGrid.currentCategory+1].length -1].style.left = '0px';
								coordArray[currentFocusGrid.currentCategory+1][coordArray[currentFocusGrid.currentCategory+1].length -1].style.display = 'flex';
							} else {
								coordArray[currentFocusGrid.currentCategory+1][currentFocusGrid.currentItem].style.top = '0';
								coordArray[currentFocusGrid.currentCategory+1][currentFocusGrid.currentItem].style.left = '0px';
								coordArray[currentFocusGrid.currentCategory+1][currentFocusGrid.currentItem].style.display = "flex";
							}
							setTimeout(function() {
								categoryArray[currentFocusGrid.currentCategory].style.left = '-100vw';
								categoryArray[currentFocusGrid.currentCategory+1].style.left = '0px';
								categoryArray[currentFocusGrid.currentCategory].style.animation = "focusOut-right 1s 1";
								categoryArray[currentFocusGrid.currentCategory+1].style.animation = "focusIn-right 1s 1";
								setTimeout(function() {
									categoryArray[currentFocusGrid.currentCategory].style.display = "none";
									categoryArray[currentFocusGrid.currentCategory].style.animation = "none";
									categoryArray[currentFocusGrid.currentCategory+1].style.animation = "none";
									setTimeout(function() {
										currentFocusGrid.currentCategory += 1;
									}, 0);
								}, 1000);
							}, 0);							
						}, 0);
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
	
	// adding EventListener for dragging: by touch
	// document.addEventListener('swiped-up', function() {
		// moveFocusLower();
	// });
	// document.addEventListener('swiped-down', function() {
		// moveFocusUpper();
	// });
	// document.addEventListener('swiped-left', function() {
		// moveFocusRightward();
	// });
	// document.addEventListener('swiped-right', function() {
		// moveFocusLeftward();
	// });
	
	// adding EventListener for dragging: by mouse
	/*
	var swipeItInstance = new SwipeIt('body');
	swipeItInstance
	.on('swipeLeft', function() {
		// console.log("left");
		moveFocusRightward();
	})
	.on('swipeUp', function() {
		// console.log("Up");
		moveFocusLower();
	})
	.on('swipeRight', function() {
		// console.log("Right");
		moveFocusLeftward();
	})
	.on('swipeDown', function() {
		// console.log("Down");
		moveFocusUpper();
	}); // reversed as is the indexing order.
	*/
	
	var swipeEventsContainer = document.getElementsByTagName('body')[0];
	var swipeEventslistener = SwipeListener(swipeEventsContainer);
	swipeEventsContainer.addEventListener('swipe', function (e) {
		var directions = e.detail.directions;
		var x = e.detail.x;
		var y = e.detail.y;
		
		if (directions.top && directions.right) {
			// console.log('Swiped top-right.');
			moveFocusLower();
		} else if (directions.bottom && directions.right) {
			// console.log('Swiped bottom-right.');
			moveFocusLeftward();
		} else if (directions.bottom && directions.left) {
			// console.log('Swiped bottom-left.');
			moveFocusUpper();
		} else if (directions.top && directions.left) {
			// console.log('Swiped top-left.');
			moveFocusRightward();
		} else if (directions.top) {
			// console.log('Swiped top.');
			moveFocusLower();
		} else if (directions.right) {
			// console.log('Swiped right.');
			moveFocusLeftward();
		} else if (directions.bottom) {
			// console.log('Swiped bottom.');
			moveFocusUpper();
		} else if (directions.left) {
			// console.log('Swiped left.');
			moveFocusRightward();
		}

		// console.log('Started horizontally at', x[0], 'and ended at', x[1]);
		// console.log('Started vertically at', y[0], 'and ended at', y[1]);
	});
	
	
	
	// Request & Response
	var jsonizedGivenDataToLoad = new Object();
	jsonizedGivenDataToLoad.connectKey = "wishket_flexSlider_20200318";
	const jsonizedGivenDataset = JSON.stringify(jsonizedGivenDataToLoad);
	
	const uriSchemeCMDtoGETformattedDataset = "http://localhost:8080/recommend/list.do";
	const proxyPagetoBypassCORS = "https://cors-anywhere.herokuapp.com/";
	
	const xmlHTTPInstanceforFlexSlider = new XMLHttpRequest();
	xmlHTTPInstanceforFlexSlider.open("POST", uriSchemeCMDtoGETformattedDataset, true);
	// xmlHTTPInstanceforFlexSlider.open("POST", proxyPagetoBypassCORS+uriSchemeCMDtoGETformattedDataset, true);
	xmlHTTPInstanceforFlexSlider.setRequestHeader("Content-Type", "application/json; charset=UTF-8");``
	xmlHTTPInstanceforFlexSlider.send(jsonizedGivenDataset);
	
	xmlHTTPInstanceforFlexSlider.onload = function() {
		if (xmlHTTPInstanceforFlexSlider.status === 200) {
			if (xmlHTTPInstanceforFlexSlider.responseText !== null && xmlHTTPInstanceforFlexSlider.responseText !== undefined && JSON.parse(xmlHTTPInstanceforFlexSlider.responseText).data !== null) {
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
});