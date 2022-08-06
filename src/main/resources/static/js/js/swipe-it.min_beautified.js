/*===========================
  Swipe-it v1.4.1
  An event listener for swiping gestures with vanilla js.
  https://github.com/tri613/swipe-it#readme
 
  @Create 2016/09/22
  @Update 2017/08/11
  @Author Trina Lu
  ===========================*/
"use strict";
var _slicedToArray = function() {
    function n(n, t) {
        var e = [],
            i = !0,
            o = !1,
            r = void 0;
        try {
            for (var u, c = n[Symbol.iterator](); !(i = (u = c.next()).done) && (e.push(u.value), !t || e.length !== t); i = !0);
        } catch (n) {
            o = !0, r = n
        } finally {
            try {
                !i && c.return && c.return()
            } finally {
                if (o) throw r
            }
        }
        return e
    }
    return function(t, e) {
        if (Array.isArray(t)) return t;
        if (Symbol.iterator in Object(t)) return n(t, e);
        throw new TypeError("Invalid attempt to destructure non-iterable instance")
    }
}();
! function(n, t, e) {
    function i(n) {
        function e() {
            o("touchstart", m, w), o("touchmove", d, w), o("touchend", p, w), E.mouseEvent && o("mousedown", s, w)
        }

        function i() {
            y = !1, D = !1, A = !1, b = !1, a = !1
        }

        function s(n) {
            a = this, y = n.clientX, D = n.clientY, o("mousemove", l, v), o("mouseup", h, v)
        }

        function l(n) {
            n.preventDefault(), y && D && (A = n.clientX, b = n.clientY)
        }

        function h(n) {
            r("mousemove", l, v), r("mouseup", h, v), p(n)
        }

        function m(n) {
            a = this, y = n.touches[0].clientX, D = n.touches[0].clientY
        }

        function d(n) {
            A = n.touches[0].clientX, b = n.touches[0].clientY
        }

        function p(n) {
            if (y && D && A && b) {
                var t = y - A,
                    e = D - b,
                    o = [t, e].map(Math.abs),
                    r = _slicedToArray(o, 2),
                    c = r[0],
                    s = r[1],
                    v = E.minDistance;
                if (c > v) {
                    var f = y < A ? "swipeRight" : "swipeLeft";
                    u(f, a, {
                        distance: t,
                        start: y,
                        end: A
                    })
                }
                if (s > v) {
                    var l = D > b ? "swipeUp" : "swipeDown";
                    u(l, a, {
                        distance: e,
                        start: D,
                        end: b
                    })
                }(c > v || s > v) && u("swipe", a)
            }
            i()
        }
        var E = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : {},
            w = c(t.querySelectorAll(n)),
            y = void 0,
            D = void 0,
            A = void 0,
            b = void 0;
        E.mouseEvent = void 0 === E.mouseEvent ? f.mouseEvent : E.mouseEvent, E.minDistance = void 0 === E.minDistance ? f.minDistance : E.minDistance, i(), e(), this.on = function(n, t) {
            return o(n, t, w), this
        }
    }

    function o(n, t, e) {
        s(e).forEach(function(e) {
            return e.addEventListener(n, t)
        })
    }

    function r(n, t, e) {
        s(e).forEach(function(e) {
            return e.removeEventListener(n, t)
        })
    }

    function u(n, e) {
        var i = arguments.length > 2 && void 0 !== arguments[2] ? arguments[2] : {},
            o = t.createEvent("Event");
        o.initEvent(n, !0, !0), o.swipe = i, s(e).forEach(function(n) {
            return n.dispatchEvent(o)
        })
    }

    function c(n) {
        for (var t = [], e = 0; e < n.length; e++) t.push(n[e]);
        return t
    }

    function s(n) {
        return Array.isArray(n) ? n : [n]
    }
    var a = !1,
        v = [n],
        f = {
            mouseEvent: !0,
            minDistance: 30
        };
    n[e] = i
}(window, document, "SwipeIt");