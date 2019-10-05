@extends('apps.layout')

@section('content')
    <h1>Data Store</h1>
    <div class="col-md-12">
    <div class="card card-default">
        <div class="card-header">
            <div class="row">
                <div class="col-md-10">
                    <strong>All Order Need Approved</strong>
                </div>
            </div>
        </div>

        <div class="card-body">
            <table class="table table-bordered" id="table_data" width="100%">
                <thead>
                    <tr>
                            <th>No.</th>
                            <th>Store Name</th>
                            <th>Owner</th>
                            <th>Address</th>
                            <th>Status</th>
                            <th width="180" class="text-center">Action</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
    

  
      <!-- /.modal -->
@stop
<script src="https://www.gstatic.com/firebasejs/5.10.1/firebase.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript" src="{{URL::asset('js/jquery.dataTables.min.js')}}"></script>
<script type="text/javascript" src="{{URL::asset('js/datatables.bootstrap.min.js')}}"></script>
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
  
  $(function () {
      var obj = [];
      var obj2 = [];
      var no = 1;
      firebase.database().ref('store/').orderByChild('status').equalTo("n").on('value', function(snapshot) {
          var order = snapshot.val();
          obj = [];  
          $.each(order, function(index ,order){
              if(order) {
                  obj2 = [no++,order.storename,order.email,order.address,order.status,'<a  data-toggle="modal" class="btn btn-success updateData" onclick="openModal(\'' + index + '\')" data-id="'+index+'">Approve</a>\
                  <a data-toggle="modal" class="btn btn-danger" onclick="openReject(\'' + index + '\')" data-id="'+index+'">Reject</a>'];
                  obj.push(obj2);
                }
            });
            addTable(obj);
            function addTable(data){
                $('#table_data').DataTable().clear().draw();
                $('#table_data').DataTable().rows.add(data).draw();
            }
        });
    });

    firebase.initializeApp(firebaseConfig);
    
    function openModal(index){
        // alert(index);
        $('#update-modal').modal('show');
        $('#id_index').val(index);
    }
    function reject(index){
        // alert(index);
        $('#rej-modal').modal('show');
        $('#id_index').val(index);
    }

    $('#body').on('click', '.updateData', function() {
        var updateID = $(this).attr('data-id');
        firebase.database().ref('store/' + updateID).on('value', function(snapshot) {
            var values = snapshot.val();
        });
    });

    function updateUserRecord() {
     var updateID = $('#id_index').val();
        firebase.database().ref('store/' +updateID).on('value', function(snapshot) {
    var data = snapshot.val();
    alert("SUKSES")
	var postData = {
        address:data.address,
        idpemilik:data.idpemilik,
        idstore:data.idstore,
        status:"y",
        storename:data.storename,
    };
    
	var updates = {};
	updates['store/' + updateID] = postData;
	firebase.database().ref().update(updates);
	$("#update-modal").modal('hide');
    });
}
    function rej() {
     var updateID = $('#id_index').val();
        firebase.database().ref('store/' +updateID).on('value', function(snapshot) {
    var data = snapshot.val();
    alert("SUKSES")
	var postData = {
        address:data.address,
        idpemilik:data.idpemilik,
        idstore:data.idstore,
        status:"t",
        storename:data.storename,
    };
    
	var updates = {};
	updates['store/' + updateID] = postData;
	firebase.database().ref().update(updates);
	$("#rej-modal").modal('hide');
    });
}

  </script>
  
  <form action="" method="POST" class="users-update-record-model form-horizontal">
    <div id="update-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog" style="width:55%;">
            <div class="modal-content" style="overflow: hidden;">
                <div class="modal-header">
                    <h4 class="modal-title" id="custom-width-modalLabel">Approve</h4>
                    <button type="button" class="close update-data-from-delete-form" data-dismiss="modal" aria-hidden="true">×</button>
                </div>
                <input type="hidden" id="id_index">
                <div class="modal-body" id="updateBody">
                    <h4>Are you sure want to approve this store?</h4>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect update-data-from-delete-form" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-success waves-effect waves-light" onclick="updateUserRecord()">Approve</button>
                </div>
            </div>
        </div>
    </div>
</form>
<form action="" method="POST" class="users-update-record-model form-horizontal">
    <div id="rej-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog" style="width:55%;">
            <div class="modal-content" style="overflow: hidden;">
                <div class="modal-header">
                    <h4 class="modal-title" id="custom-width-modalLabel">Reject</h4>
                    <button type="button" class="close update-data-from-delete-form" data-dismiss="modal" aria-hidden="true">×</button>
                </div>
                <input type="hidden" id="id_index">
                <div class="modal-body" id="updateBody">
                    <h4>Are you sure want to reject this store?</h4>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect update-data-from-delete-form" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-danger waves-effect waves-light" onclick="rej()">Reject</button>
                </div>
            </div>
        </div>
    </div>
</form>