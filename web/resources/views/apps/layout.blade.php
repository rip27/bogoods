<!DOCTYPE html>
<html>
<head>
  @include('partials.head')
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div id="wrapper">
    <nav class="navbar navbar-default navbar-fixed-top">
  <header class="main-header">
    @include('partials.header')
  </header>
  <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar">
    @include('partials.sidemenu')
  </aside>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    @yield('sectionheader')

    <!-- Main content -->
    <section class="content">
      @yield('content')
    </section>
    <!-- /.content -->
  </div>
  @include('partials.footer')
    </nav>
</div>
<!-- ./wrapper -->
  @include('partials.script')

</body>
</html>
