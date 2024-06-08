import {Component} from '@angular/core';

import {NavbarComponent} from "../navbar/navbar.component";
import {ProductDto} from "../../dtos/productDto";
import {Observable} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {ProductService} from "../../services/product.service";
import {NgIf} from "@angular/common";
import {ImageCropperComponent} from "ngx-image-cropper";
import {
  MatSnackBar,
  MatSnackBarHorizontalPosition,
  MatSnackBarVerticalPosition,
} from '@angular/material/snack-bar';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, NgForm} from "@angular/forms";

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [
    NavbarComponent,
    NgIf,
    ImageCropperComponent, MatFormFieldModule, MatSelectModule, MatButtonModule, FormsModule
  ],
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.scss']
})
export class AddProductComponent {
  product: ProductDto = {
    id: null,
    name: '',
    description: '',
    price: 0,
    user:null,
    imageData: null
  }
  form: NgForm;
  imagePreview: string | null = null; // Property for image preview
  horizontalPosition: MatSnackBarHorizontalPosition = 'end';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  constructor(
    private authService: AuthService,
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
  }

  onSubmit(form: NgForm): void {
    console.log("Creating product: " + this.product.name + " - " + this.product.description + " $" + this.product.price)
    let observable: Observable<ProductDto>;
    observable = this.productService.createProduct(this.product);
    observable.subscribe({
      next: () => {
        this.snackBar.open('Product successfully created', 'Close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          duration:3000
        });
        this.router.navigate(['/products']);

      },
      error: (error) => {
        this.snackBar.open('Error occurred while creating product ', 'Close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          duration:3000,
          panelClass: ['error-snackbar']
        });
      }
    });
  }

  onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files[0]) {
      const file = fileInput.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        const base64String = reader.result as string;
        this.imagePreview = base64String; // Set the image preview
        this.product.imageData = base64String.split(',')[1]; // Remove the MIME type prefix
      };
      reader.readAsDataURL(file);
    }
  }

}
