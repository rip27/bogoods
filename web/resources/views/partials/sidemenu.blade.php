<!-- sidebar: style can be found in sidebar.less -->
<section class="sidebar">
    <!-- Sidebar user panel -->
    <div class="user-panel">
      <div class="pull-left image">
        <img src="{{asset('dist/img/user2-160x160.jpg')}}" class="img-circle" alt="User Image">
      </div>
      <div class="pull-left info">
        @yield('name')<br><br>
        <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
      </div>
    </div>
    <!-- search form -->
    <form action="#" method="get" class="sidebar-form">
      <div class="input-group">
        <input type="text" name="q" class="form-control" placeholder="Search...">
        <span class="input-group-btn">
              <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
              </button>
            </span>
      </div>
    </form>
    <!-- /.search form -->
    <!-- sidebar menu: : style can be found in sidebar.less -->
    <ul class="sidebar-menu" data-widget="tree">
      <li class="header">MAIN NAVIGATION</li>
      <li class="active treeview">
        <a href="#">
          <i class="fa fa-dashboard"></i> <span>Data</span>
          <span class="pull-right-container">
            <i class="fa fa-angle-left pull-right"></i>
          </span>
        </a>
        <ul class="treeview-menu">
          <li class="active"><a href="{{ url('admin/datauser') }}"><i class="fa fa-circle-o"></i> Data User</a></li>
        <li><a href="{{ url('admin/datastore') }}"><i class="fa fa-circle-o"></i> Data Store</a></li>
        <li><a href="{{ url('admin/datapembayaran') }}"><i class="fa fa-circle-o"></i> Data Pembayaran</a></li>
        <li><a onclick="logout()" style="cursor: pointer"><i class="fa fa-circle-o"></i> Logout</a></li>
        </ul>
      </li>
    </ul>
  </section>
  <!-- /.sidebar -->
  <script>
    function logout(){
      firebase.auth().signOut();
      window.location.replace("{{url('login')}}");
    }
  </script>