import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: []
})
export class LoginComponent {
  titulo = 'Restaurante\'s';

  public login() {
    alert('oi');
  }
}
