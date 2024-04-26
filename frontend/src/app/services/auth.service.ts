import {Injectable} from '@angular/core';
import {AuthRequest} from "../dtos/auth-requests";
import {UserDetail} from "../dtos/auth-requests";
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {jwtDecode} from 'jwt-decode';
import {Globals} from "../globals/globals";


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';
  private event: boolean = false;

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }

  registerUser(userDetail: UserDetail): Observable<string> {
    return this.httpClient.post(this.globals.backendUri + '/register', userDetail, { responseType: 'text' })
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }

  getUser(authToken: string): Observable<UserDetail> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${authToken}`
    });

    return this.httpClient.get<UserDetail>(this.authBaseUri, { headers });
  }

  update(user: UserDetail): Observable<UserDetail> {
    return this.httpClient.put<UserDetail>(this.authBaseUri+ '/' + user.id, user);
  }

  delete(user: UserDetail): Observable<UserDetail> {
    return this.httpClient.delete<UserDetail>(this.authBaseUri + '/' + user.id)
  }


  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
      const token = this.getToken();
      const expirationDate = this.getTokenExpirationDate(token);

      // Check if token exists and expiration date is not null
      if (token && expirationDate !== null) {
          // Check if token is not expired
          return expirationDate.valueOf() > new Date().valueOf();
      }

      return false;
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
  }

  getToken() {
    return  JSON.parse(localStorage.getItem('currentUser') || '{}');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwtDecode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date | null  {

    const decoded: any = jwtDecode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  signOut(flatName: string, userId: string): Observable<string>{
    return this.httpClient.put<string>(this.authBaseUri + "/signOut/" + userId, flatName);
  }


  getUsers(userId: string): Observable<UserDetail[]> {
    return this.httpClient.get<UserDetail[]>(this.authBaseUri + "/users/" + userId);
  }

  setAdmin(selectedUserId: any): Observable<UserDetail> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };
    return this.httpClient.put<UserDetail>(this.authBaseUri + "/admin", selectedUserId, httpOptions);
  }
}
