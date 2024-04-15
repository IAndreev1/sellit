import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import {LoginComponentComponent} from "../login-component/login-component.component";
import {NavbarComponent} from "../navbar/navbar.component";

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [
    RouterLink,
    NavbarComponent
  ],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {

    protected readonly LoginComponentComponent = LoginComponentComponent;
}
