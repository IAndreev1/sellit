import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NavbarComponent} from "../navbar/navbar.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NavbarComponent
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  constructor() { }

  onSubmit(): void {
    console.log('Logging in with:', this.email, this.password);
    // Implement your authentication logic here
  }

}
