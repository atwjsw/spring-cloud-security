import { HttpInterceptor, HttpClient, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class RefreshInterceptor implements HttpInterceptor {

	constructor(private http: HttpClient) {
	}

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		return next.handle(req).pipe(
			tap(
				() => {},
				error => {
					console.log(error);
					if (error.status === 500 && error.error.message === 'Token refresh failed') {
						this.logout();
					}
				}
			)
		)
	}
	
	logout() {
		this.http.post('logout', {}).subscribe(
			() => window.location.href = 'http://auth.imooc.com:9090/logout?redirect_uri=http://admin.imooc.com:8080'
		)
	}


	

}