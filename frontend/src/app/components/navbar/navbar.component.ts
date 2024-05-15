import { Component } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {HttpClient} from "@angular/common/http";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    NgIf,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  constructor(public authService: AuthService,private httpClient: HttpClient,
              private router: Router) {
  }

  logoutUser() {
    this.authService.logoutUser();
  }
}
