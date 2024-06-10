import { Component } from '@angular/core';
import {
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  UntypedFormBuilder,
  Validators
} from "@angular/forms";
import { Router } from "@angular/router";
import { UserDetail } from "../../dtos/auth-request";

import { AuthService } from "../../services/auth.service";
import { NgIf } from "@angular/common";
import { NavbarComponent } from "../navbar/navbar.component";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    NavbarComponent,
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  submitted = false;
  error = false;
  errorMessage = '';
  horizontalPosition: MatSnackBarHorizontalPosition = 'end';
  verticalPosition: MatSnackBarVerticalPosition = 'top';
  constructor(
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.registerForm = this.formBuilder.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      password2: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  isFormValid(): boolean {
    return this.registerForm.valid;
  }

  registerUser() {
    this.submitted = true;

    if (this.registerForm.controls.password.value !== this.registerForm.controls.password2.value) {
      this.showNotification("Passwords don't match!", "error");
    } else {
      const userDetail: UserDetail = new UserDetail(
        null,
        this.registerForm.controls.firstName.value,
        this.registerForm.controls.lastName.value,
        this.registerForm.controls.email.value,
        this.registerForm.controls.password.value,
        null
      );
      console.log(userDetail);
      this.authService.registerUser(userDetail).subscribe({
        next: () => {
          console.log('Successfully registered user: ' + userDetail.email);
          this.showNotification("Successfully registered user: " + userDetail.email, "success");
          this.router.navigate(['']);
        },
        error: error => {
          console.log('Could not register due to:');
          let firstBracket = error.error.indexOf('[');
          let lastBracket = error.error.indexOf(']');
          let errorMessages = error.error.substring(firstBracket + 1, lastBracket).split(',');
          let errorDescription = error.error.substring(0, firstBracket);
          errorMessages.forEach(message => {
            this.showNotification(message, "error");
          });
        }
      });
    }
  }

  showNotification(message: string, type: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
      panelClass: type === "success" ? ['snackbar-success'] : ['snackbar-error']
    });
  }

  vanishError() {
    this.error = false;
  }

  ngOnInit(): void {
  }
}
