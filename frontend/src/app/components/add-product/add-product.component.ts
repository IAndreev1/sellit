import {Component} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {NavbarComponent} from "../navbar/navbar.component";
import {ProductDto} from "../../dtos/productDto";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {ProductService} from "../../services/product.service";

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [
    FormsModule,
    NavbarComponent
  ],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.scss'
})
export class AddProductComponent {
  product: ProductDto = {
    id:null,
    name: '',
    description: '',
    price: 0,
    imageData:null
  }
  form: NgForm;

  constructor(
    private authService: AuthService,
    private productService:ProductService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService
  ) {
  }

  onSubmit(form: NgForm): void {
    console.log("Creating product: " + this.product.name + " - " + this.product.description + " $" + this.product.price)
    let observable: Observable<ProductDto>;
    observable = this.productService.createProduct(this.product);
    observable.subscribe({
      next: () => {
        this.notification.success(`Product successfully created`, "Success");

          this.router.navigate(['/expense']);

      },
      error: (error) => {

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
        // Strip off the data URL prefix to get just the base64-encoded string
        this.product.imageData = base64String.split(',')[1];
      };
      reader.readAsDataURL(file); // Read file as a data URL
    }
  }
}
