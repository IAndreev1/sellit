import { Injectable } from '@angular/core';
import {ProductDto, ProductSearchDto} from "../dtos/productDto";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Globals} from "../global/globals";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class AllProductsService {
  private baseUri: string = this.globals.backendUri + '/product';
  constructor(private http: HttpClient, private globals: Globals,private authService: AuthService ) {
  }

  getItems( searchParameters: ProductSearchDto): Observable<ProductDto[]> {
    let params = new HttpParams();
    if (searchParameters.name) {
      params = params.append('name', searchParameters.name);
    }
    if (searchParameters.description) {
      params = params.append('description', searchParameters.description);
    }

    const headers = new HttpHeaders({
      'Authorization': this.authService.getToken()
    });
    console.log(headers)
    return this.http.get<ProductDto[]>(this.baseUri, {params,headers});
  }
}
