<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="css/form.css">
    <meta charset="utf-8">
    <title>Login Form</title>

  </head>
  <body>
    <div class="container">
      <h1>Login</h1>
      <p>Log-in or if you haven't registered yet, create an account down below</p>
      <form onsubmit="" action="index.php">
        <input type="text" placeholder="Username" class="field" id="user">
        <input type="text" placeholder="Password" class="field" id="pwd">

        <input type="submit" value="Log in" class="btn">
      </form>

      <h2>OR</h2>

      <h1>Create an Account</h1>
      <form onsubmit="" action="index.php">
        <input type="text" placeholder="Username" class="field" id="user-reg">
        <input type="text" placeholder="Password" class="field" id="pwd-reg">
        <input type="text" placeholder="E-mail" class="field" id="email-reg">

        <input type="submit" value="Log in" class="btn">
      </form>
    </div>
  </body>
</html>
