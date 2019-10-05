<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- Sidebar user panel -->
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
              <li><a href="{{ url('admin/datauser') }}"><i class="fa fa-circle-o"></i> Data User</a></li>
              <li><a href="{{ url('admin/datastore') }}"><i class="fa fa-circle-o"></i> Data Store</a></li>
              <li><a href="{{ url('admin/datapembayaran') }}"><i class="fa fa-circle-o"></i> Data Pembayaran</a></li>
              <li><a onclick="logout()" style="cursor: pointer"><i class="fa fa-circle-o"></i> Logout</a></li>
          </ul>
        </li>
      </ul>
    </section>
    <!-- /.sidebar -->
  </aside>
  <script>
      function logout(){
        firebase.auth().signOut();
        window.location.replace("{{url('login')}}");
      }
    </script>