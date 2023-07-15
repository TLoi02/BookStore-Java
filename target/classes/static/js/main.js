var navbarSupportedContent = document.getElementById('navbarSupportedContent');

// Lấy phần tử toggle button
var toggleButton = document.querySelector('.navbar-toggler');

// Bắt sự kiện click trên toggle button
toggleButton.addEventListener('click', function() {
  // Kiểm tra xem navbarSupportedContent có chứa lớp CSS 'show' hay không
  var isShow = navbarSupportedContent.classList.contains('show');

  // Nếu có lớp 'show', xóa lớp đó; ngược lại, thêm lớp 'show'
  if (isShow) {
    navbarSupportedContent.classList.remove('show');
  } else {
    navbarSupportedContent.classList.add('show');
  }
});


const swiper = new Swiper('.swiper', {
  autoplay: {
    delay: 6000, // Thời gian trễ giữa các slide (tính bằng mili giây)
  },
    // Optional parameters
    direction: 'horizontal',
    loop: true,

    // If we need pagination
    pagination: {
      el: '.swiper-pagination',
    },

    // Navigation arrows
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },

  });


  swiper.init();

