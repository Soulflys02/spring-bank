import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterOutlet } from '@angular/router';
import Keycloak from 'keycloak-js';
import { catchError, throwError } from 'rxjs';
import { CustomerApi } from './customer/customer-api';

@Component({
  imports: [RouterOutlet, MatToolbar, MatButton],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App implements OnInit {
  private readonly keycloak = inject(Keycloak);
  private readonly customerApi = inject(CustomerApi);
  protected readonly userName = this.keycloak.tokenParsed?.['name'];

  ngOnInit(): void {
    this.getCustomerOrCreate();
  }

  protected logout(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }

  protected openProfile(): void {
    this.keycloak.accountManagement();
  }

  private getCustomerOrCreate(): void {
    this.customerApi
      .getMe()
      .pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 404) {
            return this.customerApi.create();
          }
          return throwError(() => error);
        }),
      )
      .subscribe({ error: () => {} });
  }
}
