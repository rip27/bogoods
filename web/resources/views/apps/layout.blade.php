<html>
<head>
  @include('partials.head')
</head>
<body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
      @include('partials.header')
      <!-- Left side column. contains the logo and sidebar -->
      @include('partials.sidemenu')
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        @yield('sectionheader')
    
        <!-- Main content -->
        <section class="content">
          @yield('content')
        </section>
        <!-- /.content -->
      </div>
        @include('partials.footer')
     
    </div>
    <!-- ./wrapper -->
    @include('partials.script')
    </body>
</html>