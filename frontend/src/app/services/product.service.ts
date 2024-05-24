import {Injectable} from '@angular/core';
import {UserDetail} from "../dtos/auth-request";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Globals} from "../global/globals";
import {ProductDto} from "../dtos/productDto";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUri: string = this.globals.backendUri + '/product';

  constructor(private http: HttpClient, private globals: Globals,private authService: AuthService ) {
  }

  createProduct(product: ProductDto): Observable<ProductDto> {
    const headers = new HttpHeaders({
      'Authorization': this.authService.getToken()
    });
    return this.http.post<ProductDto>(this.baseUri, product,{headers});
  }

  getById(id: string): Observable<ProductDto> {
    const headers = new HttpHeaders({
      'Authorization': this.authService.getToken()
    });
    return this.http.get<ProductDto>(`${this.baseUri}/myProd/${id}`,{headers});
  }

  update(product:ProductDto): Observable<ProductDto> {
    const headers = new HttpHeaders({
      'Authorization': this.authService.getToken()
    });

    return this.http.put<ProductDto>(`${this.baseUri}`,product,{headers});
  }


}
