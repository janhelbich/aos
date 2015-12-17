
var HandleHttpError = function(response) {
  console.log('error', response);
  if (response.status === 404) {
    window.location.href = '/not-found.html';
  } else if (response.status === 401) {
    window.location.href = '/#login';
  } else {
    // window.location.href = '/error.html';
  }
}
