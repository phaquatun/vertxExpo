$(document).ready(function() {

    $('.menu').click(function() {
        $(this).toggleClass('menurolate');
        $('.navbar').toggleClass('nav-toggle');
    });

    $(window).on('load scroll', function() {
        $('.navbar').removeClass('nav-toggle');

        if ($(window).scrollTop() > 50) {
            $('header').addClass('header-active');
        } else {
            $('header').removeClass('header-active');
        }
    });

    // docment.query(relativeSelectors: DOMString)
    
});
