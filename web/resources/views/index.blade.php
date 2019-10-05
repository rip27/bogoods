<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Bogoods</title>

  <link rel="icon" href="{{asset('asset/img/logos/bogoods_with_gradient.png')}}">

  <!-- Bootstrap core CSS -->
  <link href="{{asset('asset/vendor/bootstrap/css/bootstrap.min.css')}}" rel="stylesheet">

  <!-- Custom fonts for this template -->
  <link href="{{asset('asset/vendor/fontawesome-free/css/all.min.css')}}" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
  <link href='https://fonts.googleapis.com/css?family=Kaushan+Script' rel='stylesheet' type='text/css'>
  <link href='https://fonts.googleapis.com/css?family=Droid+Serif:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
  <link href='https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700' rel='stylesheet' type='text/css'>

  <!-- Custom styles for this template -->
  <link href="{{asset('asset/css/agency.min.css')}}" rel="stylesheet">

</head>

<body id="page-top">

  <!-- Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark fixed-top" id="mainNav">
    <div class="container">
      <a class="navbar-brand js-scroll-trigger" href="#page-top">BoGoods</a>
      <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        Menu
        <i class="fas fa-bars"></i>
      </button>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav text-uppercase ml-auto">
          <li class="nav-item">
            <a class="nav-link js-scroll-trigger" href="#about">About</a>
          </li>
          <li class="nav-item">
            <a class="nav-link js-scroll-trigger" href="#team">Cara Memakai Aplikasi</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <!-- Header -->
  <header class="masthead">
    <div class="container">
      <div class="intro-text">
        <div class="intro-lead-in" id="welcomename"></div>
        <div class="intro-heading text-uppercase">Landing Page</div>
        <a class="btn btn-primary btn-xl text-uppercase js-scroll-trigger" onclick="signOut()">Signout</a>
      </div>
    </div>
  </header>

  <!-- About -->
  <section class="page-section" id="about">
    <div class="container">
      <div class="row">
        <div class="col-lg-12 text-center">
          <h2 class="section-heading text-uppercase">About</h2>
          <h3 class="section-subheading text-muted">BoGoods</h3>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <ul class="timeline">
            <li>
              <div class="timeline-image" style="background:#fff">
                <img class="rounded-circle img-fluid" src="{{asset('asset/img/logos/bogoods_with_gradient.png')}}" alt="">
              </div>
              <div class="timeline-panel">
                  <div class="timeline-heading">
                      <h4>BoGoods</h4>
                      <h4 class="subheading">Book some Goods</h4>
                    </div>
                    <div class="timeline-body">
                      <p class="text-muted">
                        Adalah Aplikasi untuk memudahkan koneksi antara Seller toko dengan Resellernya
                      </p>
                    </div>
              </div>
            </li>
            <li class="timeline-inverted">
              <div class="timeline-image" style="background:#fff">
                <img class="rounded-circle img-fluid" src="{{asset('asset/img/logos/ic_seller.png')}}" alt="">
              </div>
              <div class="timeline-panel">
                <div class="timeline-heading">
                  <h4>Seller</h4>
                  <h4 class="subheading">Fitur</h4>
                </div>
                <div class="timeline-body">
                  <p class="text-muted">
                    <ul>
                      <li> Mendaftarkan Toko Ke Aplikasi</li>
                      <li> Mengelola Data Toko Beserta Seluruh Barang yang dijual ditoko tersebut</li>
                      <li> Menerima atau Menolak Reseller yang ingin bekerja sama</li>
                      <li> Menerima Orderan</li>
                      <li> Edit Data Diri</li>
                    </ul>
                  </p>
                </div>
              </div>
            </li>
            <li>
              <div class="timeline-image" style="background:#fff">
                <img class="rounded-circle img-fluid" src="{{asset('asset/img/logos/ic_reseller.png')}}" alt="">
              </div>
              <div class="timeline-panel">
                <div class="timeline-heading">
                  <h4>Reseller</h4>
                  <h4 class="subheading">Fitur</h4>
                </div>
                <div class="timeline-body">
                  <p class="text-muted">
                      <ul>
                          <li> Meminta Request ke toko yang ingin diajak Berkerja Sama</li>
                          <li> Memasukan Barang Ke Kerangjang ( Cart )</li>
                          <li> Membuat Orderan</li>
                          <li> Edit Data Diri</li>
                        </ul>
                  </p>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>

  <!-- Team -->
  <section class="bg-light page-section" id="team">
    <div class="container">
      <div class="row">
        <div class="col-lg-12 text-center">
          <h2 class="section-heading text-uppercase">Download APK</h2>
        <a class="btn btn-secondary btn-xl text-uppercase js-scroll-trigger" download="bogoods.apk" href="{{asset('apk/bogoods.apk')}}">Download</a>
        </div>
        <div class="col-lg-12 text-center" style="margin-top:120px">
          <h2 class="section-heading text-uppercase">Tutorial Cara Pemakaian Aplikasi</h2>
        <a class="btn btn-secondary btn-xl text-uppercase js-scroll-trigger" href="{{asset('apk/Tutor_BoGoods.pdf')}}">LIHAT</a>
        </div>
      </div>
    </div>
  </section>

  <!-- Footer -->
  <footer class="footer">
    <div class="container">
      <div class="row align-items-center">
        <div class="col-md-4">
          <span class="copyright">Copyright &copy; Your Website 2019</span>
        </div>
        <div class="col-md-4">
          <ul class="list-inline social-buttons">
            <li class="list-inline-item">
              <a href="#">
                <i class="fab fa-twitter"></i>
              </a>
            </li>
            <li class="list-inline-item">
              <a href="#">
                <i class="fab fa-facebook-f"></i>
              </a>
            </li>
            <li class="list-inline-item">
              <a href="#">
                <i class="fab fa-linkedin-in"></i>
              </a>
            </li>
          </ul>
        </div>
        <div class="col-md-4">
          <ul class="list-inline quicklinks">
            <li class="list-inline-item">
              <a href="#">Privacy Policy</a>
            </li>
            <li class="list-inline-item">
              <a href="#">Terms of Use</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </footer>

  <!-- Portfolio Modals -->


  <!-- Bootstrap core JavaScript -->
  <script src="{{asset('asset/vendor/jquery/jquery.min.js')}}"></script>
  <script src="{{asset('asset/vendor/bootstrap/js/bootstrap.bundle.min.js')}}"></script>

  <!-- Plugin JavaScript -->
  <script src="{{asset('asset/vendor/jquery-easing/jquery.easing.min.js')}}"></script>

  <!-- Contact form JavaScript -->
  <script src="{{asset('asset/js/jqBootstrapValidation.js')}}"></script>
  <script src="{{asset('asset/js/contact_me.js')}}"></script>

  <!-- Custom scripts for this template -->
  <script src="{{asset('asset/js/agency.min.js')}}"></script>

</body>

</html>
<script src="https://www.gstatic.com/firebasejs/5.10.1/firebase.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script>
    var firebaseConfig = {
    apiKey: "AIzaSyB9yetTRoVua3jP89ReJzZQ4QQnKqDIrr4",
    authDomain: "bogoods.firebaseapp.com",
    databaseURL: "https://bogoods.firebaseio.com",
    projectId: "bogoods",
    storageBucket: "bogoods.appspot.com",
    messagingSenderId: "912276967847",
    appId: "1:912276967847:web:b524f8b6ff9c568c6c6682"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);

    function signout(){
        firebase.auth().signOut();
    
    }
    firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    firebase.database().ref("user/" + user.uid).once('value').then(function(snapshot){
    var name = (snapshot.val() && snapshot.val().name) || 'Anonymous';
    var phone = (snapshot.val() && snapshot.val().phone) || 'Anonymous';
    var gender = (snapshot.val() && snapshot.val().gender) || 'Anonymous';
    var email = (snapshot.val() && snapshot.val().email) || 'Anonymous';
    var foto = (snapshot.val() && snapshot.val().profile) || 'Anonymous';
    var job = (snapshot.val() && snapshot.val().job) || 'Anonymous';
    $("#welcomename").append(" Welcome " + name + "");
    $("#nameprofile").append(name);
    if(job == "seller"){
      $("#jobprofile").append("SELLER");
    }else if(job == "reseller"){
    $("#jobprofile").append("RESELLER");
    }
  });
  } else {
    window.location.href = "{{url('login')}}";
  }
});

  
</script>