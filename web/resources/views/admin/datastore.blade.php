@extends('apps.layout')

@section('content')

    <h1>Data Store</h1>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="card card-default">
                    <div class="card-header">
                        <div class="row">
                            <div class="col-md-10">
                                <strong>All Store Listing</strong>
                            </div>
                        </div>
                    </div>
    
                    <div class="card-body">
                        <table class="table table-bordered">
                            <tr>
                                <th>No.</th>
                                <th>Store Name</th>
                                <th>Owner</th>
                                <th>Address</th>
                                <th>Status</th>
                                <th width="180" class="text-center">Action</th>
                            </tr>
                            <tbody id="tbody">
                                
                            </tbody>	
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    

  
      <!-- /.modal -->
@stop
<script src="https://www.gstatic.com/firebasejs/5.10.1/firebase.js"></script>
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

  var databaseRef = firebase.database().ref('store/');
  var lastIndex = 0;

  databaseRef.on("value", function(snapshot) {
    var value = snapshot.val();
    var htmls = [];
    var no = 1;
    $.each(value, function(index, value){
    	if(value) {
                htmls.push('<tr>\
        		<td>'+ no++ +'</td>\
        		<td>'+ value.storename +'</td>\
        		<td>'+ value.idpemilik +'</td>\
        		<td>'+ value.address +'</td>\
        		<td>'+ value.status +'</td>\
        		<td><a data-toggle="modal" data-target="#action" class="btn btn-outline-success" data-id="'+value.idstore+'">Action</a></td>\
        	    </tr>');
                $('#tbody').html(htmls);
    	}    	
    	lastIndex = index;
    });

    var idupdate = 0;
    $('.updateStore').on('click', function() {
        idupdate = $(this).attr("data-id");
        $("#storename").val(idupdate);
    firebase.database().ref('store/' + idupdate).on('value', function(snapshot) {
    var ustatus = $("#status").val();
    var data = snapshot.val();
	var postData = {
        status:ustatus,
        address:data.address,
        idpemilik:data.idpemilik,
        idstore:data.idstore,
        storename:data.storename
    };
    
	var updates = {};
	updates['store/' + idupdate] = postData;
	firebase.database().ref().update(updates);
	$("#action").modal('hide');
});
});
  });

  </script>
  
  <div class="modal fade" id="modal-default">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title">Default Modal</h4>
        </div>
        <div class="modal-body">
          <p>One fine body&hellip;</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary">Save changes</button>
        </div>
      </div>
      <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
  </div>
{{-- Modal Action --}}
    <div id="action" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" 
    aria-hidden="true" style="display: none;">
        <div class="modal-dialog" style="width:55%;">
            <div class="modal-content" style="overflow: hidden;">
                <div class="modal-header">
                    <h4 class="modal-title" id="custom-width-modalLabel">Action</h4>
                    <button type="button" class="close update-data-from-delete-form" data-dismiss="modal" 
                    aria-hidden="true">×</button>
                </div>
                <div class="modal-body" id="updateAction">
                        <div class="form-group has-feedback">
                                <input id="storename" type="text" readonly class="form-control">
                                <span class="glyphicon glyphicon-store form-control-feedback"></span>
                              </div>
                    <select name="status" id="status" class="form-control">
                        <option value="y">Approve</option>
                        <option value="t">Reject</option>
                    </select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default waves-effect pull-left update-data-from-delete-form" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-success waves-effect waves-light updateStore">Update</button>
                </div>
            </div>
        </div>
    </div>