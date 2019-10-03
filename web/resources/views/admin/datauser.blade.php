@extends('apps.layout')

@section('content')

    <h1>Data User</h1>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="card card-default">
                    <div class="card-header">
                        <div class="row">
                            <div class="col-md-10">
                                <strong>All Users Listing</strong>
                            </div>
                        </div>
                    </div>
    
                    <div class="card-body">
                        <table class="table table-bordered">
                            <tr>
                                <th>No.</th>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Phone</th>
                                <th>Email</th>
                                <th>Job</th>
                            </tr>
                            <tbody id="tbody">
                                
                            </tbody>	
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    

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

var databaseRef = firebase.database().ref('user/');
var lastIndex = 0;

databaseRef.on("value", function(snapshot) {
  var values = snapshot.val();
  var htmls = [];
  var uid = firebase.auth().currentUser.uid
  var no = 1;
  $.each(values, function(index, values){
      if(values) {
          if(values.id != uid){
            htmls.push('<tr>\
              <td>'+ no++ +'</td>\
              <td>'+ values.id +'</td>\
              <td>'+ values.name +'</td>\
              <td>'+ values.phone +'</td>\
              <td>'+ values.email +'</td>\
              <td>'+ values.job +'</td>\
              </tr>');
            }
      }    	
      lastIndex = index;
  });
  $('#tbody').html(htmls);
});

  </script>